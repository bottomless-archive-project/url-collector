package com.github.bottomlessarchive.urlcollector.task.service;

import com.github.bottomlessarchive.urlcollector.parser.service.WarcParser;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkUnitProcessor {

    private final HttpClient httpClient;
    private final WarcParser warcParser;

    public Set<String> process(final WorkUnit workUnit) {
        log.info("Started working on location: {}.", workUnit.getLocation());

        final Path tempFilePath = Path.of(System.getProperty("java.io.tmpdir"))
                .resolve(workUnit.getId() + ".warc");

        log.info("Saving WARC file to path: {}.", tempFilePath);

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(workUnit.getLocation()))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofFile(tempFilePath));

            try (final InputStream warcContent = Files.newInputStream(tempFilePath)) {
                log.info("Finished downloading for {} location, starting to parse.", workUnit.getLocation());

                return warcParser.parseWarcFile(warcContent);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to process work unit: " + workUnit.getId() + "!");

            return Collections.emptySet();
        } finally {
            log.info("Finished parsing location: {}, deleting temp file at {}.",
                    workUnit.getLocation(), tempFilePath);

            try {
                Files.delete(tempFilePath);
            } catch (IOException e) {
                log.error("Failed to delete temp file on path: " + tempFilePath + "!");
            }
        }
    }
}
