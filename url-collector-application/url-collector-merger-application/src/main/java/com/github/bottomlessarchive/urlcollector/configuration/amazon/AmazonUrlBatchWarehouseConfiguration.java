package com.github.bottomlessarchive.urlcollector.configuration.amazon;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.bottomlessarchive.urlcollector.serializer.service.UrlBatchSerializer;
import com.github.bottomlessarchive.urlcollector.uploader.service.URLBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.uploader.service.amazon.AmazonUrlBatchWarehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonUrlBatchWarehouseConfiguration {

    private final AmazonConfigurationProperties awsAmazonConfigurationProperties;

    @Bean
    public URLBatchWarehouse uploaderBatchUploader(final AmazonS3 amazonS3, final UrlBatchSerializer urlBatchSerializer) {
        return new AmazonUrlBatchWarehouse(awsAmazonConfigurationProperties.getBucketName(), amazonS3, urlBatchSerializer);
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(awsAmazonConfigurationProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        awsAmazonConfigurationProperties.getAccessKey(),
                                        awsAmazonConfigurationProperties.getSecretKey()
                                )
                        )
                )
                .build();
    }
}
