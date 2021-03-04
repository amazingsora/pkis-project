package com.tradevan.apcommon.bean;

import java.io.Serializable;

/**
 * Title: QueryBean<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.6
 */
public class QueryBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sysId;
	private String queryStr;
	private String querySource;
	private String queryType;
	private String excelCols;
	private String excelColsTitle;
	private String nowUserId;
	private String backUrl;
	
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getQuerySource() {
		return querySource;
	}

	public void setQuerySource(String querySource) {
		this.querySource = querySource;
	}
	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getExcelCols() {
		return excelCols;
	}

	public void setExcelCols(String excelCols) {
		this.excelCols = excelCols;
	}

	public String getExcelColsTitle() {
		return excelColsTitle;
	}

	public void setExcelColsTitle(String excelColsTitle) {
		this.excelColsTitle = excelColsTitle;
	}

	public String getNowUserId() {
		return nowUserId;
	}

	public void setNowUserId(String nowUserId) {
		this.nowUserId = nowUserId;
	}
	
	public String getBackUrl() {
		return backUrl;
	}
	
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
}
