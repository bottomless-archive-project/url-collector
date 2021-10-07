package com.github.bottomlessarchive.urlcollector.validator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("validation")
public class ValidationConfigurationProperties {

    private List<String> types;
}