package com.eim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
@MapperScan(value="com.eim.mapper")
@SpringBootApplication
public class Sjf20180424Application extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Sjf20180424Application.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(Sjf20180424Application.class, args);
	}
}
