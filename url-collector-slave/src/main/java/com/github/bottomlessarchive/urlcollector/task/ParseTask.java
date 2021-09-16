package com.github.bottomlessarchive.urlcollector.task;

import com.github.bottomlessarchive.urlcollector.parser.service.WarcParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

@Slf4j
@RequiredArgsConstructor
public class ParseTask implements Callable<Set<String>> {

    private final String location;
    private final HttpClient httpClient;
    private final WarcParser warcParser;

    @Override
    public Set<String> call() throws Exception {
        log.info("Started working on location: {}.", location);

        final Path tempFilePath = Path.of(System.getProperty("java.io.tmpdir"))
                .resolve(UUID.randomUUID() + ".warc");

        log.info("Saving WARC file to path: {}.", tempFilePath);

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(location))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofFile(tempFilePath));

            try (final InputStream warcContent = Files.newInputStream(tempFilePath)) {
                log.info("Finished downloading for {} locaton, starting to parse.", location);

                return warcParser.parseWarcFile(warcContent);
            }
        } finally {
            log.info("Finished parsing, deleting temp file at {}.", tempFilePath);

            Files.delete(tempFilePath);
        }
    }
}
