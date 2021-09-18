package com.github.bottomlessarchive.urlcollector.command;

import com.github.bottomlessarchive.urlcollector.task.service.WorkUnitProcessor;
import com.github.bottomlessarchive.urlcollector.uploader.ResultUploader;
import com.github.bottomlessarchive.urlcollector.validator.service.ResultValidator;
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
    private final ResultValidator resultValidator;
    private final ResultUploader resultUploader;
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
                final Set<String> result = workUnitProcessor.process(workUnit).stream()
                        .filter(resultValidator::validateResult)
                        .collect(Collectors.toSet());

                log.info("Starting uploading for task: {}.", workUnit.getId());

                resultUploader.uploadResult(workUnit.getId().toString(), result);

                log.info("Finished uploading for task: {}, reporting as finished.", workUnit.getId());

                workUnitClient.finishWorkUnit(workUnit);

                commandRateLimitingSemaphore.release();
            });
        }
    }
}
