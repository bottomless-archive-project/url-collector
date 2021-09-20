package com.github.bottomlessarchive.urlcollector.command;

import com.github.bottomlessarchive.urlcollector.task.service.WorkUnitProcessor;
import com.github.bottomlessarchive.urlcollector.uploader.UrlUploader;
import com.github.bottomlessarchive.urlcollector.validator.service.UrlValidator;
import com.github.bottomlessarchive.urlcollector.workunit.service.WorkUnitClient;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlCollectorCommand implements CommandLineRunner {

    private final WorkUnitClient workUnitClient;
    private final UrlValidator urlValidator;
    private final UrlUploader urlUploader;
    private final WorkUnitProcessor workUnitProcessor;
    private final Semaphore commandRateLimitingSemaphore;
    private final ExecutorService commandExecutorService;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            commandRateLimitingSemaphore.acquire();

            log.info("Starting new work unit.");

            final WorkUnit workUnit = workUnitClient.startWorkUnit();

            log.info("Got work unit: {}.", workUnit);

            commandExecutorService.execute(() -> {
                final Set<String> resultUrls = workUnitProcessor.process(workUnit).stream()
                        .filter(urlValidator::validateUrl)
                        .collect(Collectors.toSet());

                log.info("Starting uploading for task: {}.", workUnit.getId());

                urlUploader.uploadUrls(workUnit.getId().toString(), resultUrls);

                log.info("Finished uploading for task: {}, reporting as finished.", workUnit.getId());

                workUnitClient.finishWorkUnit(workUnit);

                commandRateLimitingSemaphore.release();
            });
        }
    }
}
