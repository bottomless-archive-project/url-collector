package com.github.bottomlessarchive.urlcollector.workunit.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
public class WorkUnit {

    private final UUID id;
    private final String location;
    private final WorkUnitStatus status;
}
