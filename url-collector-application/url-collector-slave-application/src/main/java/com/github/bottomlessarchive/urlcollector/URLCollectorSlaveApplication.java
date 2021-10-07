package com.github.bottomlessarchive.urlcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class URLCollectorSlaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(URLCollectorSlaveApplication.class, args);
	}
}
