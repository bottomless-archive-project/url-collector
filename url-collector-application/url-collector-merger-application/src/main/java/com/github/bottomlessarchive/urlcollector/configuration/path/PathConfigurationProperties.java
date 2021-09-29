package com.github.bottomlessarchive.urlcollector.configuration.path;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("result")
public class PathConfigurationProperties {

    private String path;
}
