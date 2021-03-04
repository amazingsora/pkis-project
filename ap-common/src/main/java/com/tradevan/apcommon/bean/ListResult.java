package com.tradevan.apcommon.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Title: ListResult<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class ListResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<?> data;
	
	public ListResult(List<?> data) {
		super();
		this.data = data;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
}
