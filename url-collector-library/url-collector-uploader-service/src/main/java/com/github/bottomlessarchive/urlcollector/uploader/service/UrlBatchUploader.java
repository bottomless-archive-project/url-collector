package com.github.bottomlessarchive.urlcollector.uploader.service;

import java.util.Set;

public interface UrlBatchUploader {

    void uploadUrls(String batchId, Set<String> result);
}
