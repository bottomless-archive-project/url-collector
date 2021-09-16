package com.github.bottomlessarchive.urlcollector.crawl.service;

import com.github.bottomlessarchive.commoncrawl.WarcLocationFactory;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnitStatus;
import com.github.bottomlessarchive.urlcollector.workunit.service.work.WorkUnitService;
import com.github.bottomlessarchive.urlcollector.configuration.crawl.UrlsConfigurationProperties;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrawlService {

    private final WarcLocationFactory warcLocationFactory;
    private final WorkUnitService workUnitService;
    private final UrlsConfigurationProperties urlsConfigurationProperties;

    public void initializeCrawl(final String crawlId) {
        final List<String> workUnitLocations = warcLocationFactory.buildLocationStringStream(crawlId)
                .toList();

        final List<WorkUnit> workUnits = Lists.partition(workUnitLocations, urlsConfigurationProperties.getBatchSize()).stream()
                .map(location -> WorkUnit.builder()
                        .id(UUID.randomUUID())
                        .locations(new HashSet<>(location))
                        .status(WorkUnitStatus.CREATED)
                        .build()
                )
                .toList();

        workUnitService.createWorkUnit(workUnits);
    }
}
