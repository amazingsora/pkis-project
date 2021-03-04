package com.tradevan.apcommon.bean;

import java.io.Serializable;

/**
 * Title: NameValuePair<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class NameValuePair implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	
	public NameValuePair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof NameValuePair) {
			NameValuePair bean = (NameValuePair) obj;
			if (value == null) {
				return (bean.value == null);
			}
			else {
				return value.equals(bean.value);
			}
		}
		return false;
    }
}
