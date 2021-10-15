package com.github.bottomlessarchive.urlcollector.uploader.service.amazon.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("warehouse.aws")
public class AmazonConfigurationProperties {

    private String region;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
