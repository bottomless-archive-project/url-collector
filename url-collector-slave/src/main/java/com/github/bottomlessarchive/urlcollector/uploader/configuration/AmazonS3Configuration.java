package com.github.bottomlessarchive.urlcollector.uploader.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Configuration {

    private final AmazonS3ConfigurationProperties awsS3ConfigurationProperties;

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
