package com.github.bottomlessarchive.urlcollector.workunit.service;

import com.github.bottomlessarchive.urlcollector.http.JsonBodyHandler;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.StartWorkUnitResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkUnitClient {

    private final HttpClient httpClient;
    private final WorkUnitRequestFactory workUnitRequestFactory;

    @SneakyThrows
    public WorkUnit startWorkUnit() {
        final HttpRequest request = workUnitRequestFactory.newStartWorkUnitRequest();

        final StartWorkUnitResponse workUnitResponse = httpClient.send(request,
                        JsonBodyHandler.jsonBodyHandler(StartWorkUnitResponse.class))
                .body()
                .get();

        return WorkUnit.builder()
                .id(UUID.fromString(workUnitResponse.getId()))
                .location(workUnitResponse.getLocation())
                .build();
    }

    @SneakyThrows
    public void finishWorkUnit(final WorkUnit workUnit) {
        final HttpRequest request = workUnitRequestFactory.newFinishWorkUnitRequest(workUnit);

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }
}
