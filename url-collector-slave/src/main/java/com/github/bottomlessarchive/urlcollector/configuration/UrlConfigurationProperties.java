package com.github.bottomlessarchive.urlcollector.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("urls")
public class UrlConfigurationProperties {

    private List<String> types;
}