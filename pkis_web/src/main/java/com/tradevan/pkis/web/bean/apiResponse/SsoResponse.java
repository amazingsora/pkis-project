package com.tradevan.pkis.web.bean.apiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SsoResponse {
	
	private String RETURNCODE;
	
	private String RETURNMSG;
	
	private SsoDataResponse RETURNDATA;

	@JsonProperty("RETURNCODE")
	public String getRETURNCODE() {
		return RETURNCODE;
	}

	public void setRETURNCODE(String rETURNCODE) {
		RETURNCODE = rETURNCODE;
	}

	@JsonProperty("RETURNMSG")
	public String getRETURNMSG() {
		return RETURNMSG;
	}

	public void setRETURNMSG(String rETURNMSG) {
		RETURNMSG = rETURNMSG;
	}

	@JsonProperty("RETURNDATA")
	@JsonInclude(Include.NON_NULL)
	public SsoDataResponse getRETURNDATA() {
		return RETURNDATA;
	}

	public void setRETURNDATA(SsoDataResponse rETURNDATA) {
		RETURNDATA = rETURNDATA;
	}
}
