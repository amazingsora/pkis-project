package com.tradevan.pkis.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@MapperScan({"com.tradevan.mapper"})
//@EntityScan("com.tradevan.jpa")
@EntityScan(basePackages = {"com.tradevan.jpa","com.tradevan.aporg.model","com.tradevan.handyflow.model"})
//@EnableJpaRepositories("com.tradevan.jpa")
@EnableJpaRepositories(basePackages = {"com.tradevan.jpa","com.tradevan.aporg.repository","com.tradevan.handyflow.repository"})
@ComponentScan("com.tradevan")
public class PKISApplication extends SpringBootServletInitializer {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		setRegisterErrorPageFilter(false);
        return application.sources(PKISApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(PKISApplication.class,  "--debug");
    }
	
    @Value("${sso.cross.origin}")
    String ssoCrossOrigin ;
    
    @Value("${getSupplierIdApi.cross.origin}")
    String suppApiCrossOrigin;
    
    @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/common/api/getSupplierId").allowedOrigins(suppApiCrossOrigin.split(","));
				registry.addMapping("/common/api/sso").allowedOrigins(ssoCrossOrigin.split(","));
				registry.addMapping("/common/api/sso/page").allowedOrigins(ssoCrossOrigin.split(","));
			}
		};
	}
}

