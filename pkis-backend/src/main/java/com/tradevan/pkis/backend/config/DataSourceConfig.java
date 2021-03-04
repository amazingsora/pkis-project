package com.tradevan.pkis.backend.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.tradevan.util.TvEncrypt;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	
	Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private Environment env;
	
	@Value("${spring.profiles.active}")
	private String profile;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String p;
	
	@Value("${spring.datasource.driverClassName}")
	private String driver;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	private String orapassPath;
	
	private String applicationId;

	@Bean(destroyMethod = "close")
	@Primary
	public DataSource dataSource() {
		if (profile.equals("valid") || profile.equals("prod")) {
			
			orapassPath = env.getProperty("tv.env.orapassPath");
			applicationId = env.getProperty("tv.env.applicationId");
			
			BufferedReader bufferedReader = null;
			try {
				if (StringUtils.isNotBlank(orapassPath)) {
					File file = new File(orapassPath);
					if (file.exists() && file.isFile()) {
						bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
						String lineTxt = null;
						while ((lineTxt = bufferedReader.readLine()) != null) {
							if (lineTxt.startsWith(applicationId)) {
								String[] arr = StringUtils.split(lineTxt, " ") ;
								userName = arr[1];
								p = TvEncrypt.decode(arr[2]);
								break;
							}						
						}
					}
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {				
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		final HikariDataSource ds = new HikariDataSource();
		ds.setDriverClassName(driver);
        ds.setJdbcUrl(url);
        ds.setUsername(userName);
        ds.setPassword(p);
        ds.setIdleTimeout(60000);
        ds.setConnectionTimeout(60000);
        ds.setValidationTimeout(3000);
        ds.setMaxLifetime(1800000);
        ds.setMaximumPoolSize(15); 
        return ds;

	}
	
}
