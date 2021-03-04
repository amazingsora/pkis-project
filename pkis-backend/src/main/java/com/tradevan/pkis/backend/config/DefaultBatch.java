package com.tradevan.pkis.backend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration(exclude = { 
		JmxAutoConfiguration.class
})
@MapperScan({"com.tradevan.mapper"})
@EntityScan("com.tradevan.aporg.model")
@EnableJpaRepositories("com.tradevan.aporg.repository.impl")
@EnableTransactionManagement
@ComponentScan({
	"com.tradevan"
})
public class DefaultBatch {

}
