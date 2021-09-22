package com.github.bottomlessarchive.urlcollector.workunit.service;

import com.github.bottomlessarchive.urlcollector.workunit.configuration.MasterServerConfigurationProperties;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnitStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkUnitRequestFactoryTest {

    @Mock
    private MasterServerConfigurationProperties masterServerConfigurationProperties;

    @InjectMocks
    private WorkUnitRequestFactory workUnitRequestFactory;

    @BeforeEach
    void setup() {
        when(masterServerConfigurationProperties.getHost())
                .thenReturn("example.com");
        when(masterServerConfigurationProperties.getPort())
                .thenReturn(2000);
    }

    @Test
    void testNewStartWorkUnitRequest() {
        final HttpRequest httpRequest = workUnitRequestFactory.newStartWorkUnitRequest();

        assertThat(httpRequest.uri().toString())
                .hasToString("http://example.com:2000/work-unit/start-work");
        assertThat(httpRequest.headers().allValues("Accept").size())
                .isEqualTo(1);
        assertThat(httpRequest.headers().allValues("Accept").get(0))
                .isEqualTo("application/json");
        assertThat(httpRequest.method())
                .contains("POST");

        final Optional<HttpRequest.BodyPublisher> bodyPublisher = httpRequest.bodyPublisher();

        assertThat(bodyPublisher)
                .isPresent();
        assertThat(bodyPublisher.get().contentLength())
                .isZero();
    }

    @Test
    void testNewFinishWorkUnitRequest() {
        final WorkUnit workUnit = WorkUnit.builder()
                .id(UUID.fromString("1b39b03e-8583-412b-b26e-fbbea979fc3d"))
                .location("http://example.com/test/test.warc")
                .status(WorkUnitStatus.UNDER_PROCESSING)
                .build();

        final HttpRequest httpRequest = workUnitRequestFactory.newFinishWorkUnitRequest(workUnit);

        assertThat(httpRequest.uri().toString())
                .hasToString("http://example.com:2000/work-unit/finish-work");
        assertThat(httpRequest.headers().allValues("Accept").size())
                .isEqualTo(1);
        assertThat(httpRequest.headers().allValues("Accept").get(0))
                .isEqualTo("application/json");
        assertThat(httpRequest.method())
                .contains("POST");

        final Optional<HttpRequest.BodyPublisher> bodyPublisher = httpRequest.bodyPublisher();

        assertThat(bodyPublisher)
                .isPresent();

        FlowSubscriber<ByteBuffer> flowSubscriber = new FlowSubscriber<>();
        bodyPublisher.get().subscribe(flowSubscriber);

        byte[] actual = flowSubscriber.getBodyItems().get(0).array();

        assertThat(new String(actual))
                .isEqualTo("{\"id\":\"1b39b03e-8583-412b-b26e-fbbea979fc3d\"}");
    }
}