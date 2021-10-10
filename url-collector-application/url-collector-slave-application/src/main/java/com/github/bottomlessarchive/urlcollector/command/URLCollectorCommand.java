package com.github.bottomlessarchive.urlcollector.command;

import com.github.bottomlessarchive.urlcollector.task.service.WorkUnitProcessor;
import com.github.bottomlessarchive.urlcollector.uploader.service.URLBatchWarehouse;
import com.github.bottomlessarchive.urlcollector.validator.service.URLValidator;
import com.github.bottomlessarchive.urlcollector.workunit.service.WorkUnitClient;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class URLCollectorCommand implements CommandLineRunner {

    private final WorkUnitClient workUnitClient;
    private final URLValidator urlValidator;
    private final URLBatchWarehouse urlBatchWarehouse;
    private final WorkUnitProcessor workUnitProcessor;
    private final Semaphore commandRateLimitingSemaphore;
    private final ExecutorService commandExecutorService;

    @Override
    public void run(final String... args) throws Exception {
        while (true) {
            commandRateLimitingSemaphore.acquire();

            log.info("Starting new work unit.");

            final Optional<WorkUnit> optionalWorkUnit = workUnitClient.startWorkUnit();

            if (optionalWorkUnit.isEmpty()) {
                TimeUnit.MINUTES.sleep(10);

                log.info("Got no work unit! Waiting a minute.");

                continue;
            }

            final WorkUnit workUnit = optionalWorkUnit.get();

            log.info("Got work unit: {}.", workUnit);

            commandExecutorService.execute(() -> {
                final Set<String> resultUrls = workUnitProcessor.process(workUnit).stream()
                        .filter(urlValidator::validateUrl)
                        .collect(Collectors.toSet());

                log.info("Starting uploading for task: {}.", workUnit.getId());

                urlBatchWarehouse.saveUrls(workUnit.getId(), resultUrls);

                log.info("Finished uploading for task: {}, reporting as finished.", workUnit.getId());

                workUnitClient.finishWorkUnit(workUnit);

                commandRateLimitingSemaphore.release();
            });
        }
    }
}
