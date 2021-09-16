package com.github.bottomlessarchive.urlcollector.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws")
public class AwsS3ConfigurationProperties {

    private String region;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
