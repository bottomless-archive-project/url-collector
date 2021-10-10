package com.github.bottomlessarchive.urlcollector.uploader.service.local;

import com.github.bottomlessarchive.urlcollector.serializer.service.UrlBatchSerializer;
import com.github.bottomlessarchive.urlcollector.uploader.service.URLBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.uploader.service.local.configuration.LocalConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "warehouse.type", havingValue = "local")
public class LocalURLBatchWarehouse implements URLBatchWarehouse {

    private final UrlBatchSerializer urlBatchSerializer;
    private final LocalConfigurationProperties localConfigurationProperties;

    @Override
    public void saveUrls(final UUID batchId, final Set<String> result) {
        final Path targetPath = Path.of(localConfigurationProperties.getTargetDirectory())
                .resolve("dataset-" + batchId.toString() + ".ubds");

        final byte[] targetContent = urlBatchSerializer.serializeUrls(result);

        log.debug("Initializing file save to an local filesystem based file warehouse! Target filename: " + targetPath
                + " file size: " + targetContent.length + ".");

        try {
            Files.write(targetPath, targetContent);
        } catch (Exception e) {
            log.error("Failed to save payload to local filesystem based file repository! Target filename: "
                    + targetPath + " file size: " + targetContent.length + "!");

            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> loadUrls(final UUID batchId) {
        final Path targetPath = Path.of(localConfigurationProperties.getTargetDirectory())
                .resolve("dataset-" + batchId.toString() + ".ubds");

        log.debug("Initializing file load from an local filesystem based file warehouse! Target filename: "
                + targetPath + ".");

        try {
            return urlBatchSerializer.deserializeUrls(Files.readAllBytes(targetPath));
        } catch (IOException e) {
            log.error("Failed to read payload from an local filesystem based file warehouse! Target filename: "
                    + targetPath + "!");

            throw new RuntimeException(e);
        }
    }
}
