package com.github.bottomlessarchive.urlcollector.configuration.repository;

import com.github.bottomlessarchive.urlcollector.workunit.repository.domain.WorkUnitDatabaseEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoCollectionConfiguration {

    @Bean
    public MongoCollection<WorkUnitDatabaseEntity> workUnitDatabaseEntityMongoCollection(
            final MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("work_units", WorkUnitDatabaseEntity.class);
    }
}
