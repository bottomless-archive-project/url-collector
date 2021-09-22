package com.github.bottomlessarchive.urlcollector.workunit.service;

import com.github.bottomlessarchive.urlcollector.http.JsonBodyHandler;
import com.github.bottomlessarchive.urlcollector.workunit.configuration.MasterServerConfigurationProperties;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.FinishWorkUnitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpRequest;

@Service
@RequiredArgsConstructor
public class WorkUnitRequestFactory {

    private final MasterServerConfigurationProperties masterServerConfigurationProperties;

    public HttpRequest newStartWorkUnitRequest() {
        return HttpRequest.newBuilder(
                        URI.create("http://" + masterServerConfigurationProperties.getHost() + ":"
                                + masterServerConfigurationProperties.getPort() + "/work-unit/start-work")
                )
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    }

    public HttpRequest newFinishWorkUnitRequest(final WorkUnit workUnit) {
        final FinishWorkUnitRequest finishWorkUnitRequest = FinishWorkUnitRequest.builder()
                .id(workUnit.getId().toString())
                .build();

        return HttpRequest.newBuilder(
                        URI.create("http://" + masterServerConfigurationProperties.getHost() + ":"
                                + masterServerConfigurationProperties.getPort() + "/work-unit/finish-work")
                )
                .header("Accept", "application/json")
                .POST(JsonBodyHandler.jsonBodyPublisher(finishWorkUnitRequest))
                .build();
    }
}
