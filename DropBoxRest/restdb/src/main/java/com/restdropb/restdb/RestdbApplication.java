package com.restdropb.restdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.restdropb.restdb")
public class RestdbApplication {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext =SpringApplication.run(RestdbApplication.class, args);
		checkBeansPresence("DropboxClient");
	}

	private static void checkBeansPresence(String... beans) {
        for (String beanName : beans) {
            System.out.println("Is " + beanName + " in ApplicationContext: " + 
              applicationContext.containsBean(beanName));
        }
    }

}
