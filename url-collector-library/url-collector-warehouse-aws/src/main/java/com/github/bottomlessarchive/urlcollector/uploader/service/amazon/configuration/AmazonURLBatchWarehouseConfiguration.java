package com.github.bottomlessarchive.urlcollector.uploader.service.amazon.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.bottomlessarchive.urlcollector.serializer.service.UrlBatchSerializer;
import com.github.bottomlessarchive.urlcollector.uploader.service.URLBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.uploader.service.amazon.AmazonURLBatchWarehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "warehouse.type", havingValue = "aws")
public class AmazonURLBatchWarehouseConfiguration {

    private final AmazonConfigurationProperties awsS3ConfigurationProperties;

    @Bean
    public URLBatchWarehouse uploaderBatchUploader(final AmazonS3 amazonS3,
                                                   final UrlBatchSerializer urlBatchSerializer) {
        return new AmazonURLBatchWarehouse(awsS3ConfigurationProperties.getBucketName(), amazonS3, urlBatchSerializer);
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
