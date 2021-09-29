package com.github.bottomlessarchive.urlcollector.uploader.service.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.github.bottomlessarchive.urlcollector.serializer.service.UrlBatchSerializer;
import com.github.bottomlessarchive.urlcollector.uploader.service.UrlBatchWarehouse;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class AmazonUrlBatchWarehouse implements UrlBatchWarehouse {

    private final String bucketName;
    private final AmazonS3 amazonS3;
    private final UrlBatchSerializer urlBatchSerializer;

    public AmazonUrlBatchWarehouse(final String bucketName, final AmazonS3 amazonS3,
                                   final UrlBatchSerializer urlBatchSerializer) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
        this.urlBatchSerializer = urlBatchSerializer;
    }

    @Override
    public void uploadUrls(final UUID batchId, final Set<String> result) {
        writeFile("crawled-data/dataset-" + batchId + ".ubds", urlBatchSerializer.serializeUrls(result));
    }

    @Override
    public Set<String> downloadUrls(final UUID batchId) {
        return urlBatchSerializer.deserializeUrls(readFile("crawled-data/dataset-" + batchId + ".ubds"));
    }

    private void writeFile(final String fileName, final byte[] fileData) {
        log.debug("Initializing file upload to an AWS based file repository! Target filename: " + fileName
                + " file size: " + fileData.length + " target bucket: " + bucketName + ".");

        try {
            final ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentLength(fileData.length);
            objectMetadata.setContentType("application/x-lzma");

            amazonS3.putObject(bucketName, fileName, new ByteArrayInputStream(fileData), objectMetadata);
        } catch (Exception e) {
            log.error("Failed to upload payload to AWS based file repository! Target filename: " + fileName
                    + " file size: " + fileData.length + " target bucket: " + bucketName + "!");

            throw e;
        }
    }

    private byte[] readFile(final String fileName) {
        log.debug("Initializing file download from an AWS based file repository! Target filename: " + fileName
                + " target bucket: " + bucketName + ".");

        try {
            return amazonS3.getObject(bucketName, fileName).getObjectContent().readAllBytes();
        } catch (IOException e) {
            log.error("Failed to read payload from AWS based file repository! Target filename: " + fileName
                    + " target bucket: " + bucketName + "!");

            throw new RuntimeException("Failed to read payload from AWS based file repository!", e);
        }
    }
}
