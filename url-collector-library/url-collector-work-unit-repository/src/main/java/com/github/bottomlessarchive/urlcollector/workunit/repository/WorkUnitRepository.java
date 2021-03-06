package com.github.bottomlessarchive.urlcollector.workunit.repository;

import com.github.bottomlessarchive.urlcollector.workunit.repository.domain.WorkUnitDatabaseEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class WorkUnitRepository {

    private final MongoCollection<WorkUnitDatabaseEntity> documentDatabaseEntityMongoCollection;

    public void createWorkUnits(final List<WorkUnitDatabaseEntity> workUnitDatabaseEntity) {
        documentDatabaseEntityMongoCollection.insertMany(workUnitDatabaseEntity);
    }

    public Optional<WorkUnitDatabaseEntity> findById(final UUID documentId) {
        return Optional.ofNullable(
                documentDatabaseEntityMongoCollection.find(Filters.eq("_id", documentId))
                        .first()
        );
    }

    public List<WorkUnitDatabaseEntity> findAll() {
        final Iterable<WorkUnitDatabaseEntity> iterator = documentDatabaseEntityMongoCollection.find();

        final List<WorkUnitDatabaseEntity> workUnitDatabaseEntities = new LinkedList<>();

        for (WorkUnitDatabaseEntity workUnitDatabaseEntity : iterator) {
            workUnitDatabaseEntities.add(workUnitDatabaseEntity);
        }

        return workUnitDatabaseEntities;
    }

    public Optional<WorkUnitDatabaseEntity> startWorkUnit() {
        return Optional.ofNullable(
                documentDatabaseEntityMongoCollection.findOneAndUpdate(
                        Filters.eq("status", "CREATED"),
                        Updates.set("status", "UNDER_PROCESSING")
                )
        );
    }

    public void finishWorkUnit(final UUID documentId) {
        documentDatabaseEntityMongoCollection.updateOne(Filters.eq("_id", documentId),
                Updates.set("status", "FINISHED"));
    }
}
