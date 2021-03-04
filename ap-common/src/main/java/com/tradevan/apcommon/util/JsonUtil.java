package com.tradevan.apcommon.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.text.DateFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: JsonUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.3
 */
public class JsonUtil {
	protected static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	public static String toJsonString(Object object) {
		return toJsonString(object, null, false);
	}
	
	public static String toJsLiteral(Object object) {
		return toJsonString(object, null, true);
	}

	public static String toJsonString(Object object, DateFormat myDateFormat, boolean jsLiterals) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		
		if (myDateFormat != null) {
			mapper.setDateFormat(myDateFormat);
		}
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		if (jsLiterals) {
			mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		}

		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return jsonString;
	}
	
	public static String filterIllegalChar(String jsonStr) {
		if (StringUtils.isNotBlank(jsonStr)) {
			//return jsonStr.replaceAll("\\\\n|\\\\r|\\\\t", " ").replaceAll("\\\\'|\\\\\"|\\\\&|\\\\b|\\\\f|[\u0000-\u0019]", "");
			return jsonStr.replaceAll("\\\\n", "\\\\\\\\n").replaceAll("\\\\r", "\\\\\\\\r").replaceAll("\\\\t", "\\\\\\\\t").replaceAll("\\\\'", "\\\\\\\\'")
					.replaceAll("\\\\\"", "\\\\\\\\\"").replaceAll("\\\\&", "\\\\\\\\&").replaceAll("\\\\b", "\\\\\\\\b").replaceAll("\\\\f", "\\\\\\\\f")
					.replaceAll("[\u0000-\u0019]", "").replaceAll("'", "\\\\'");
		}
		else {
			return jsonStr;
		}
	}
	
	public static String replaceBackslash(String jsonStr) {
		return replaceBackslash(jsonStr, true);
	}
	
	public static String replaceBackslash(String jsonStr, boolean keepNewline) {
		if (StringUtils.isNotBlank(jsonStr)) {
			//return jsonStr.replaceAll("\\\\+[^\\\\n]", "");
			String str = null;
			if (keepNewline) {
				str = jsonStr.replaceAll("\\\\r\\\\n", "/WINNEWLINE");
				str = jsonStr.replaceAll("\\\\r", "/IOSNEWLINE");
				str = str.replaceAll("\\\\n", "/UNIXNEWLINE");
			}
			else {
				str = jsonStr;
			}
			str = str.replaceAll("\\\\\\\\", "＼");
			//str = str.replaceAll("\\\\", "");
			if (keepNewline) {
				str = str.replaceAll("/WINNEWLINE", "\\\\r\\\\n");
				str = str.replaceAll("/IOSNEWLINE", "\\\\r");
				str = str.replaceAll("/UNIXNEWLINE", "\\\\n");
			}
			return str;
		}
		else {
			return jsonStr;
		}
	}
}
