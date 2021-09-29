package com.github.bottomlessarchive.urlcollector.serializer.service.lzma;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bottomlessarchive.urlcollector.serializer.service.UrlBatchSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LZMAUrlBatchSerializer implements UrlBatchSerializer {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] serializeUrls(final Set<String> urls) {
        // Sorting the list, to optimize the compression
        final List<String> sortedUrls = asSortedList(urls);

        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (final LZMACompressorOutputStream lzmaStream = new LZMACompressorOutputStream(
                    new BufferedOutputStream(byteArrayOutputStream))) {
                objectMapper.writeValue(lzmaStream, sortedUrls);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (final IOException e) {
            log.error("Failed to serialize urls!", e);

            throw new RuntimeException("Failed to serialize urls!", e);
        }
    }

    @Override
    public Set<String> deserializeUrls(final byte[] data) {
        try {
            final InputStream contentInputStream = new LZMACompressorInputStream(new ByteArrayInputStream(data));

            return objectMapper.readValue(contentInputStream, new TypeReference<>() {
            });
        } catch (final IOException e) {
            throw new RuntimeException("Error while decompressing document!", e);
        }
    }

    private <T extends Comparable<? super T>> List<T> asSortedList(final Collection<T> c) {
        final List<T> list = new ArrayList<>(c);

        Collections.sort(list);

        return list;
    }
}
