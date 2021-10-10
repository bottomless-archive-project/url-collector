package com.github.bottomlessarchive.urlcollector.uploader.service.local.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("warehouse.local")
public class LocalConfigurationProperties {

    private String targetDirectory;
}
