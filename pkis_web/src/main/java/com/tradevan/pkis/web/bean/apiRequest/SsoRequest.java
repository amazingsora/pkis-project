package com.tradevan.pkis.web.bean.apiRequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SsoRequest {
	
	private String USERID;
	
	private List<String> SPID;

	@JsonProperty("USERID")
	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	@JsonProperty("SPID")
	public List<String> getSPID() {
		return SPID;
	}

	public void setSPID(List<String> sPID) {
		SPID = sPID;
	}
}
