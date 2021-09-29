package com.github.bottomlessarchive.urlcollector.uploader.service;

import java.util.Set;

public interface UrlBatchWarehouse {

    void uploadUrls(String batchId, Set<String> result);

    Set<String> downloadUrls(String batchId);
}
