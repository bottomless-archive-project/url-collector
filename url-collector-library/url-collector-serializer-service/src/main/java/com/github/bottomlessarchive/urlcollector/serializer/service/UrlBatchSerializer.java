package com.github.bottomlessarchive.urlcollector.serializer.service;

import java.util.Set;

public interface UrlBatchSerializer {

    byte[] serializeUrls(Set<String> urls);

    Set<String> deserializeUrls(byte[] data);
}
