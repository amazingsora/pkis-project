package com.tradevan.pkis.web.xss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.tradevan.xauthframework.common.utils.JsonUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	Logger logger = LogManager.getLogger(XssHttpServletRequestWrapper.class);
	
    private HttpServletRequest orgRequest = null;
    private static final Whitelist whitelist = Whitelist.basicWithImages();
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(orgRequest.getInputStream()));
        List<String> lines = IOUtils.readLines(orgRequest.getInputStream());
        String line = "";
        for(String sLine : lines) {
        	line += sLine ;
        }
        String result = "";
        if (line != null) {
            result += clean(line);
        }
        return new WrappedServletInputStream(new ByteArrayInputStream(result.getBytes()));
    }

    @Override
    public String getParameter(String name) {
        if (("content".equals(name) || name.endsWith("WithHtml"))) {
            return super.getParameter(name);
        }
        name = clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = clean(value);
        }
        return value;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Map getParameterMap() {
    	Map<String, String[]> paramMap = super.getParameterMap();
    	Map<String, String[]> returnMap = new HashMap<String, String[]>();
    	
    	try {
    		for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String[] values = (String[]) entry.getValue();
                String[] nValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {                	
                    if (values[i] instanceof String) {        
                    	String v = values[i];
                    	if (StringUtils.isNotBlank(v)) {
                    		if (JsonUtils.isJsonFormat(v)) {
                        		Map<String, String> data = (Map) JsonUtils.json2Object(v, Map.class, false);
                        		Map<String, String> d = new HashMap<String, String>();
                        		for (Iterator it = data.entrySet().iterator(); it.hasNext(); ) {
                        			Map.Entry et = (Map.Entry) it.next();      
                        			if (et.getValue() instanceof String) {
                        				d.put(et.getKey().toString(), clean(et.getValue().toString()));
                        			}                    			
                        		}
                        		if (d != null && d.size() > 0) {
                        			nValues[i] = JsonUtils.obj2json(d);
                        		}                    		
                        	}
                        	else {
                        		nValues[i] = clean(v);
                        	}  
                    	}                    	                 
                    }
                }
                returnMap.put(entry.getKey().toString(), nValues);
            }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}    	
    	
        return returnMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = clean(arr[i]);
            }
        }
        return arr;
    }

    @Override
    public String getHeader(String name) {
        name = clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            value = clean(value);
        }
        return value;
    }

    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

    public String clean(String content) {
        String result = Jsoup.clean(content, "", whitelist, outputSettings);
        return result;
    }

    private class WrappedServletInputStream extends ServletInputStream {
        @SuppressWarnings("unused")
		public void setStream(InputStream stream) {
            this.stream = stream;
        }

        private InputStream stream;

        public WrappedServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
    
}
