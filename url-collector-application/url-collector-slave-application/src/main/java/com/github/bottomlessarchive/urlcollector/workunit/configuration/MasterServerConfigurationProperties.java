package com.github.bottomlessarchive.urlcollector.workunit.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("master")
public class MasterServerConfigurationProperties {

    private String host;
    private int port;
}
