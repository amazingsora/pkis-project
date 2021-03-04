package com.tradevan.apcommon.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.tradevan.apcommon.bean.NameValuePair;

/**
 * Title: BeanUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.2
 */
public class BeanUtil extends BeanUtils {
	
	public static void copyPropertiesIgnoreNull(Object source, Object target, String... ignores) {
		copyProperties(source, target, getNullPropertyNames(source, ignores));
	}
	
	public static String[] getNullPropertyNames(Object source, String... ignores) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		for (String ignore : ignores) {
			emptyNames.add(ignore);
		}
		String[] result = new String[emptyNames.size() + ignores.length];
		return emptyNames.toArray(result);
	}
	
	public static boolean isNotEquals(String bean1, String bean2) {
		if ((bean1 == null && bean2 != null) || (bean1 != null && bean2 == null) || (bean1 != null && bean2 != null && !bean1.equals(bean2))) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEquals(BigDecimal bean1, BigDecimal bean2) {
		if ((bean1 == null && bean2 != null) || (bean1 != null && bean2 == null) || (bean1 != null && bean2 != null && bean1.compareTo(bean2) != 0)) {
			return true;
		}
		return false;
	}
	
	public static Long convert2Long(Object result) {
		if (result instanceof Long) {
			return (Long) result; 
		}
		else if (result instanceof BigDecimal) {
			return ((BigDecimal) result).longValue(); 
		}
		else if (result instanceof BigInteger) {
			return ((BigInteger) result).longValue();
		}
		else if (result instanceof Short) {
			return ((Short) result).longValue();
		}
		else if (result instanceof Integer) {
			return ((Integer) result).longValue();
		}
		else if (result instanceof String){
			return Long.valueOf((String) result);
		}
		else {
			return null;
		}
	}
	
	public static List<NameValuePair> arrangeValue2First(List<NameValuePair> nameValuePairList, String value) {
		NameValuePair found = null;
		int idx = -1;
		if (value != null) {
			for (int x = 0; x < nameValuePairList.size(); x++) {
				if (nameValuePairList.get(x).getValue().equals(value)) {
					found = nameValuePairList.get(x);
					idx = x;
					break;
				}
			}
			if (found != null) {
				nameValuePairList.add(0, found);
				nameValuePairList.remove(idx + 1);
			}
		}
		return nameValuePairList;
	}
	
}
