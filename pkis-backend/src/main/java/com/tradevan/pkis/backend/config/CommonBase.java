package com.tradevan.pkis.backend.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

public class CommonBase {
	
	@Value("${xauth.appId}")
	public String APP_ID;

	protected Log logger = LogFactory.getLog(getClass());
}
