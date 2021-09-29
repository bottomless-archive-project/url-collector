package com.github.bottomlessarchive.urlcollector.workunit.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;

@ExtendWith(MockitoExtension.class)
class WorkUnitClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private WorkUnitRequestFactory workUnitRequestFactory;

    @InjectMocks
    private WorkUnitClient workUnitClient;

}