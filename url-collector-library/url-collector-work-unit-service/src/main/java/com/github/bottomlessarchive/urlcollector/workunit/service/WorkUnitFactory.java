package com.github.bottomlessarchive.urlcollector.workunit.service;

import com.github.bottomlessarchive.urlcollector.workunit.repository.WorkUnitRepository;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkUnitFactory {

    private final WorkUnitRepository workUnitRepository;

    public Optional<WorkUnit> getWorkUnit(final UUID workUnitId) {
        return workUnitRepository.findById(workUnitId)
                .map(workUnitDatabaseEntity -> WorkUnit.builder()
                        .id(workUnitDatabaseEntity.getId())
                        .location(workUnitDatabaseEntity.getLocation())
                        .status(WorkUnitStatus.valueOf(workUnitDatabaseEntity.getStatus()))
                        .build()
                );
    }
}
