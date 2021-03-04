package com.tradevan.pkis.web.enums;

import com.tradevan.xauthframework.core.common.LocaleMessage;

public enum IDEN_TYPE {

	/**
	 * 系統
	 */
	SYSTEM ("00", LocaleMessage.getMsg("iden.type.system")),
	
	/**
	 * 通路
	 */
	DISTRIBUTOR ("01", LocaleMessage.getMsg("iden.type.distributor")),
	
	/**
	 * 供應商
	 */
	SUPPLIER ("02", LocaleMessage.getMsg("iden.type.supplier"));
	
	private String code ;
	
	private String message ;

	IDEN_TYPE(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
