package com.github.bottomlessarchive.urlcollector.uploader.service.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bottomlessarchive.urlcollector.uploader.service.UrlBatchWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;

import java.io.*;
import java.util.*;

@Slf4j
public class AmazonUrlBatchWarehouse implements UrlBatchWarehouse {

    private final String bucketName;
    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper;

    public AmazonUrlBatchWarehouse(final String bucketName, final AmazonS3 amazonS3, final ObjectMapper objectMapper) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
        this.objectMapper = objectMapper;
    }

    @Override
    public void uploadUrls(final UUID batchId, final Set<String> result) {
        // Sorting the list, to optimize the compression
        final List<String> urls = asSortedList(result);

        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            //TODO: Move the serializer to an UrlSerialize class
            // Write the data to an LZMA stream (encoding it in the process).
            try (final LZMACompressorOutputStream lzmaStream = new LZMACompressorOutputStream(
                    new BufferedOutputStream(byteArrayOutputStream))) {
                objectMapper.writeValue(lzmaStream, urls);
            }

            // Uploading the encoded result.
            writeFile("crawled-data/dataset-" + batchId + ".ubds", byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            log.error("Failed to upload!", e);
        }
    }

    @Override
    public Set<String> downloadUrls(final UUID batchId) {
        try {
            final InputStream contentInputStream = new LZMACompressorInputStream(readFile(
                    "crawled-data/dataset-" + batchId + ".ubds"));

            return objectMapper.readValue(contentInputStream, new TypeReference<>() {
            });
        } catch (final IOException e) {
            throw new RuntimeException("Error while decompressing document!", e);
        }
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

    private InputStream readFile(final String fileName) {
        log.debug("Initializing file download from an AWS based file repository! Target filename: " + fileName
                + " target bucket: " + bucketName + ".");

        try {
            return amazonS3.getObject(bucketName, fileName).getObjectContent();
        } catch (Exception e) {
            log.error("Failed to read payload from AWS based file repository! Target filename: " + fileName
                    + " target bucket: " + bucketName + "!");

            throw e;
        }
    }

    private <T extends Comparable<? super T>> List<T> asSortedList(final Collection<T> c) {
        final List<T> list = new ArrayList<>(c);

        Collections.sort(list);

        return list;
    }
}
