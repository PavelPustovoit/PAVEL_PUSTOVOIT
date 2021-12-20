package com.restdropb.restdb.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.restdropb.restdb.*" })
public class Config {
    @Autowired
	private Environment env;
}
