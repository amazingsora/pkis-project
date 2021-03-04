package com.tradevan.pkis.web.config;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	
	@Autowired
	private Environment env;
	
	@Value("${spring.profiles.active}")
	private String profile;

	private String userName;
	
	private String p;

	private String driver;

	private String url;
	
	private String jndiName;
	
	@Bean(destroyMethod = "close")
	@Primary
	public DataSource dataSource() throws Exception {		
		HikariConfig hikariConfig = new HikariConfig(); 	
		
		jndiName = env.getProperty("spring.datasource.jndi-name");
		if (StringUtils.isNotBlank(jndiName)) {	    
		    hikariConfig.setDataSource((DataSource) new InitialContext().lookup(jndiName));		    
		    final HikariDataSource ds = new HikariDataSource(hikariConfig);
	        return ds;
		}
		else {
			driver = env.getProperty("spring.datasource.driverClassName");			
			url = env.getProperty("spring.datasource.url");
			userName = env.getProperty("spring.datasource.username");
			p = env.getProperty("spring.datasource.password");
			
			final HikariDataSource ds = new HikariDataSource();
			ds.setDriverClassName(driver);
	        ds.setJdbcUrl(url);
	        ds.setUsername(userName);
	        ds.setPassword(p);
	        return ds;
		}
	}
	
}
