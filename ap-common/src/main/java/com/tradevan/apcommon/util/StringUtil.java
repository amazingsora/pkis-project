package com.tradevan.apcommon.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Title: StringUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.6
 */
public class StringUtil extends StringUtils {

	public static String concatByComma(List<String> list) {
		return concat(list, ",", null);
	}
	
	public static String concatByComma(List<String> list, String replacement) {
		return concat(list, ",", replacement);
	}
	
	public static String concat(List<String> list, String symbol, String replacement) {
		StringBuilder buf = new StringBuilder("");
		boolean first = true;
		for (String data : list) {
			if (first) {
				first = false;
			}
			else {
				buf.append(",");
			}
			
			if (replacement != null) {
				buf.append(replacement);
			}
			else {
				buf.append(data);
			}
		}
		return buf.toString();
	}
	
	public static List<?> parseToListByComma(String data) {
		return parseToList(data, ",");
	}
	
	public static List<?> parseToList(String data, String symbol) {
		if (data != null) {
			List<String> list = new ArrayList<String>();
			for (String val : data.split(symbol)) {
				list.add(val);
			}
			return list;
		}
		return Collections.EMPTY_LIST;
	}
	
	public static boolean isValuesExistInTokens(String values, String tokens) {
		if (tokens != null && values != null) {
			StringTokenizer st0 = new StringTokenizer(values, ",");
			while (st0.hasMoreTokens()) {
				String value  = st0.nextToken();
				StringTokenizer st1 = new StringTokenizer(tokens, ",");
				while (st1.hasMoreTokens()) {
					if (value.equals(st1.nextToken())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isValuesNotExistInTokens(String values, String tokens) {
		return !isValuesExistInTokens(values, tokens);
	}
	
	public static boolean isValuesContains(String values, String token) {
		if (token != null && values != null) {
			StringTokenizer st0 = new StringTokenizer(values, ",");
			while (st0.hasMoreTokens()) {
				String value  = st0.nextToken();
				if (value.equals(token)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isValuesNotContains(String values, String token) {
		return !isValuesContains(values, token);
	}
	
	public static boolean isEmail(String email) {
		if (email != null && Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).matches()) {
			return true;
		}
		return false;
	}
	
	public static String formatMsg(String msg, Object... params) {
		if(isNotBlank(msg) && params != null) {
			return new MessageFormat(msg).format(params);
		}
		return msg;
	}
	
	public static String replaceNullToSpace(String str) {
		if ((StringUtils.isBlank(str))|| ("null".equalsIgnoreCase(str.trim()))) {
			return "";
		}else {
			return str;
		}
	}
	
	public static String trunc(String data, String start, String end) {
		int idx1 = data.indexOf(start);
		if (idx1 > 0) {
			int idx2 = data.indexOf(end, idx1);
			if (idx2 > 0) {
				return data.substring(0, idx1) + data.substring(idx2 + end.length());
			}
		}
		return data;
	}
	
	/**
	 * 允許全形空白
	 * @param value
	 * @return
	 */
	public static boolean isBlank2(String value) {
		if (StringUtils.isBlank(value) && value.indexOf("　") == -1) {
			return true;
		}
		return false;
	}
	
	public static String blankIf(String value, String value2) {
		if (StringUtils.isBlank(value)) {
			return value2;
		}
		return value;
	}
	
	/**
	 * 取after之後的值
	 * @param value
	 * @param after(預設-);
	 * @return
	 */
	public static String rightAfter(String value, String... after) {
		if (StringUtil.isNotBlank(value)) {
			if (after[0] != null) {
				int idx = value.indexOf(after[0]);
				if (idx != -1) {
					value = value.substring(idx + after[0].length());
				}
			}
			else {
				int idx = value.indexOf("-");
				if (idx != -1) {
					value = value.substring(idx + 1);
				}
			}
		}
		return value;
	}
	
}
