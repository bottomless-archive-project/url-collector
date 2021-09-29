package com.github.bottomlessarchive.urlcollector.crawl.view.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InitializeCrawlRequest {

    String crawlId;
}
