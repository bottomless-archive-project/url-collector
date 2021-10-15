package com.github.bottomlessarchive.urlcollector.crawl.service;

import com.github.bottomlessarchive.commoncrawl.WarcLocationFactory;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnitStatus;
import com.github.bottomlessarchive.urlcollector.workunit.service.WorkUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlService {

    private final WarcLocationFactory warcLocationFactory;
    private final WorkUnitService workUnitService;

    public void initializeCrawl(final String crawlId) {
        log.info("Initializing crawl with Common Crawl id: {}.", crawlId);

        final List<WorkUnit> workUnitLocations = warcLocationFactory.buildLocationStringStream(crawlId)
                .map(location -> WorkUnit.builder()
                        .id(UUID.randomUUID())
                        .location(location)
                        .status(WorkUnitStatus.CREATED)
                        .build()
                )
                .toList();

        workUnitService.createWorkUnit(workUnitLocations);
    }
}
