package com.github.bottomlessarchive.urlcollector.uploader.service;

import java.util.Set;
import java.util.UUID;

public interface UrlBatchWarehouse {

    void uploadUrls(UUID batchId, Set<String> result);

    Set<String> downloadUrls(UUID batchId);
}
