package com.github.bottomlessarchive.urlcollector.configuration.crawl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urls")
public class UrlsConfigurationProperties {

    private int batchSize;
}
