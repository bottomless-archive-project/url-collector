package com.github.bottomlessarchive.urlcollector.validator.service;

import com.github.bottomlessarchive.urlcollector.parser.configuration.UrlConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class UrlValidator {

    private final UrlConfigurationProperties urlConfigurationProperties;

    public boolean validateUrl(final String result) {
        if (result.isEmpty()) {
            return false;
        }

        try {
            final URI sourceLocation = new URI(result);

            return hasTargetExtension(sourceLocation);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private boolean hasTargetExtension(final URI sourceLocation) {
        final String path = sourceLocation.getPath();

        if (path == null) {
            return false;
        }

        return urlConfigurationProperties.getTypes().stream()
                .anyMatch(type -> path.endsWith("." + type));
    }
}
