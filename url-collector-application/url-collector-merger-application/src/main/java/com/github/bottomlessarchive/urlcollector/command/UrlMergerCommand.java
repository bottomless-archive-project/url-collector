package com.github.bottomlessarchive.urlcollector.command;

import com.github.bottomlessarchive.urlcollector.configuration.path.PathConfigurationProperties;
import com.github.bottomlessarchive.urlcollector.uploader.service.UrlBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.workunit.service.WorkUnitFactory;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlMergerCommand implements CommandLineRunner {

    private final WorkUnitFactory workUnitFactory;
    private final UrlBatchWarehouse urlBatchWarehouse;
    private final PathConfigurationProperties pathConfigurationProperties;

    @Override
    public void run(final String... args) throws IOException {
        final List<WorkUnit> workUnits = workUnitFactory.getWorkUnits();

        final Set<String> uniqueUrls = new HashSet<>();
        int iterationCount = 0;
        for (WorkUnit workUnit : workUnits) {
            iterationCount++;

            final Set<String> urls = urlBatchWarehouse.downloadUrls(workUnit.getId());

            log.info("Adding {} urls as the {} iteration in batch: {} to the result set. Items already in the set: {}.",
                    urls.size(), iterationCount, workUnit.getId(), uniqueUrls.size());

            uniqueUrls.addAll(urls);
        }

        log.info("Writing {} urls to file!", uniqueUrls.size());

        final Path pathToResult = Path.of(pathConfigurationProperties.getPath())
                .resolve("result.ubds");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(pathToResult.toFile()))) {
            for (String value : uniqueUrls) {
                out.write(value);
                out.newLine();
            }
        }
    }
}
