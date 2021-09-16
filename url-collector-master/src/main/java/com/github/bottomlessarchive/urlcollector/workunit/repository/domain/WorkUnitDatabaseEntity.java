package com.github.bottomlessarchive.urlcollector.workunit.repository.domain;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Set;
import java.util.UUID;

@Data
public class WorkUnitDatabaseEntity {

    @BsonId
    private UUID id;
    private Set<String> locations;
    private String status;
}
