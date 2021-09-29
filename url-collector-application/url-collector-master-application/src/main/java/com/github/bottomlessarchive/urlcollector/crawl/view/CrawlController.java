package com.github.bottomlessarchive.urlcollector.crawl.view;

import com.github.bottomlessarchive.urlcollector.crawl.service.CrawlService;
import com.github.bottomlessarchive.urlcollector.crawl.view.request.InitializeCrawlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlService crawlService;

    @PostMapping
    public void initializeCrawl(@RequestBody final InitializeCrawlRequest initializeCrawlRequest) {
        crawlService.initializeCrawl(initializeCrawlRequest.getCrawlId());
    }
}
