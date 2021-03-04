package com.tradevan.pkis.web.bean.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SsoDataResponse {

	private String TOKEN;

	@JsonProperty("TOKEN")
	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}
}
