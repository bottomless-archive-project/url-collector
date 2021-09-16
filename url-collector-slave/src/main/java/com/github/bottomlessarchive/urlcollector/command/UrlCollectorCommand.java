package com.github.bottomlessarchive.urlcollector.command;

import com.github.bottomlessarchive.urlcollector.task.ParseTask;
import com.github.bottomlessarchive.urlcollector.task.service.ParseTaskFactory;
import com.github.bottomlessarchive.urlcollector.uploader.ResultUploader;
import com.github.bottomlessarchive.urlcollector.validator.service.ResultValidator;
import com.github.bottomlessarchive.urlcollector.workunit.service.WorkUnitClient;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlCollectorCommand implements CommandLineRunner {

    private final WorkUnitClient workUnitClient;
    private final ResultValidator resultValidator;
    private final ResultUploader resultUploader;
    private final ParseTaskFactory parseTaskFactory;
    private final ExecutorService executorService = Executors.newFixedThreadPool(72);

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            log.info("Starting new work unit.");

            final WorkUnit workUnit = workUnitClient.startWorkUnit();

            log.info("Got work unit: {}.", workUnit);

            final List<ParseTask> locationTasks = workUnit.getLocations().stream()
                    .map(parseTaskFactory::newTask)
                    .toList();

            final Set<String> result = executorService.invokeAll(locationTasks).stream()
                    .flatMap(future -> {
                        try {
                            return future.get().stream();
                        } catch (ExecutionException | InterruptedException e) {
                            log.error("Failed to parse location!", e);

                            return Stream.empty();
                        }
                    })
                    .filter(resultValidator::validateResult)
                    .collect(Collectors.toSet());

            log.info("Finished parsing every location. Result size: {}. Starting the uploading.", result.size());

            resultUploader.uploadResult(workUnit.getId().toString(), result);

            log.info("Finished uploading, reporting the finished task.");

            workUnitClient.finishWorkUnit(workUnit);
        }
    }
}
