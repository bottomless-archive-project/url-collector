package com.github.bottomlessarchive.urlcollector.uploader.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bottomlessarchive.urlcollector.uploader.service.UrlBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.uploader.service.amazon.AmazonUrlBatchWarehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Configuration {

    private final AmazonS3ConfigurationProperties awsS3ConfigurationProperties;

    @Bean
    public UrlBatchWarehouse uploaderBatchUploader(final AmazonS3 amazonS3, final ObjectMapper objectMapper) {
        return new AmazonUrlBatchWarehouse(awsS3ConfigurationProperties.getBucketName(), amazonS3, objectMapper);
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(awsS3ConfigurationProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        awsS3ConfigurationProperties.getAccessKey(),
                                        awsS3ConfigurationProperties.getSecretKey()
                                )
                        )
                )
                .build();
    }
}
