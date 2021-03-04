package com.tradevan.pkis.web.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("SendApiUtil")
@Transactional(rollbackFor=Exception.class)
public class SendApiUtil {

	public String post(String postUrl, Map<String, String> paramMap, File file) {
		String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
		
		try {
			URL url = new URL(settingUrlParams(postUrl, paramMap));
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			ds = new DataOutputStream(urlConnection.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + file.getName() + "\"" + end);
			ds.writeBytes(end);

			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			fStream.close();
			
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
			
			int responseCode = urlConnection.getResponseCode();
			
			System.out.println(String.format("code:%s, message:%s", responseCode, urlConnection.getResponseMessage()));
			
			if(responseCode == HttpURLConnection.HTTP_OK){
				inputStream = urlConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
			}else{
				System.out.println("result:" + responseCode);
			}
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}finally {
			if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
			
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		
		return replaceString(resultBuffer.toString());
	}
	
	private String settingUrlParams(String url, Map<String, String> paramMap){
		boolean isFirst = true;
		StringBuffer bf = new StringBuffer(url);
		
		try{
			for(String key : paramMap.keySet()){
				String value = paramMap.get(key);
				bf.append(isFirst ? "?" : "&").append(key).append("=").append(value);
				isFirst = false;
			}
		}catch(Exception e){
			e.getMessage();
		}
		
		return bf.toString();
	}
	
	private String replaceString(String resultStr) {
		String resultJson = resultStr;
		if(StringUtils.isNotBlank(resultJson)){
			resultJson = resultJson.replaceAll("\\\\", "");
			resultJson = resultJson.substring(resultJson.indexOf("\"")+1, resultJson.lastIndexOf("\""));
		}
		
		return resultJson;
	}
}
