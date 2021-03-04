package com.tradevan.pkis.junit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan({"com.tradevan.mapper"})
@EntityScan("com.tradevan.jpa")
@EnableJpaRepositories("com.tradevan.jpa")
@ComponentScan("com.tradevan")
public class PKISApplicationUnitTest {

	public static void main(String[] args) {
		SpringApplication.run(PKISApplicationUnitTest.class, args);
	}

}
