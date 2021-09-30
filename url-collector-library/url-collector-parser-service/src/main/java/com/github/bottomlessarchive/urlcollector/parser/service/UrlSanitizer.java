package com.github.bottomlessarchive.urlcollector.parser.service;

import org.springframework.stereotype.Service;

@Service
public class UrlSanitizer {

    public String sanitize(final String url) {
        return url.trim();
    }
}
