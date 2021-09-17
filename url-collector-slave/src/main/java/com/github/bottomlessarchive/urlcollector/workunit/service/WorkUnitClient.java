package com.github.bottomlessarchive.urlcollector.workunit.service;

import com.github.bottomlessarchive.urlcollector.configuration.MasterServerConfigurationProperties;
import com.github.bottomlessarchive.urlcollector.http.JsonBodyHandler;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.FinishWorkUnitRequest;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.StartWorkUnitResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkUnitClient {

    private final HttpClient httpClient;
    private final MasterServerConfigurationProperties masterServerConfigurationProperties;

    @SneakyThrows
    public WorkUnit startWorkUnit() {
        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://" + masterServerConfigurationProperties.getHost() + ":"
                                + masterServerConfigurationProperties.getPort() + "/work-unit/start-work")
                )
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

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
        final FinishWorkUnitRequest finishWorkUnitRequest = FinishWorkUnitRequest.builder()
                .id(workUnit.getId().toString())
                .build();

        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://" + masterServerConfigurationProperties.getHost() + ":"
                                + masterServerConfigurationProperties.getPort() + "/work-unit/finish-work")
                )
                .header("Accept", "application/json")
                .POST(JsonBodyHandler.jsonBodyPublisher(finishWorkUnitRequest))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }
}
