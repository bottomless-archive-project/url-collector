package com.github.bottomlessarchive.urlcollector.workunit.repository.domain;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.UUID;

@Data
public class WorkUnitDatabaseEntity {

    @BsonId
    private UUID id;
    private String location;
    private String status;
}
