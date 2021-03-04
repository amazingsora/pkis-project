package com.tradevan.pkis.backend.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.tradevan.pkis.backend.config.DefaultBatch;
import com.tradevan.pkis.backend.service.SampleService;

public class SampleBatch extends DefaultBatch {

	public static void main(String[] args) {
		try {
			ApplicationContext ctx = SpringApplication.run(SampleBatch.class, args);		
			SampleService sampleService = (SampleService) ctx.getBean("SampleService");
			sampleService.process();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
