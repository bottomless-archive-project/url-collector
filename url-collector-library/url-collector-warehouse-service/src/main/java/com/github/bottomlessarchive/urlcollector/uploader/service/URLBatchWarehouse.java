package com.github.bottomlessarchive.urlcollector.uploader.service;

import java.util.Set;
import java.util.UUID;

public interface URLBatchWarehouse {

    void saveUrls(UUID batchId, Set<String> result);

    Set<String> loadUrls(UUID batchId);
}
