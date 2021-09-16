package com.github.bottomlessarchive.urlcollector.task.service;

import com.github.bottomlessarchive.urlcollector.parser.service.WarcParser;
import com.github.bottomlessarchive.urlcollector.task.ParseTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParseTaskFactory {

    private final WarcParser warcParser;
    private final HttpClient httpClient;

    public ParseTask newTask(final String location) {
        log.info("Creating task for location: {}.", location);

        return new ParseTask(location, httpClient, warcParser);
    }
}
