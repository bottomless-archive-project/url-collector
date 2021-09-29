package com.github.bottomlessarchive.urlcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UrlCollectorMergerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlCollectorMergerApplication.class, args);
    }
}
