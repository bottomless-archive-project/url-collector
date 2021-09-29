package com.github.bottomlessarchive.urlcollector.uploader.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws")
public class AmazonConfigurationProperties {

    private String region;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
