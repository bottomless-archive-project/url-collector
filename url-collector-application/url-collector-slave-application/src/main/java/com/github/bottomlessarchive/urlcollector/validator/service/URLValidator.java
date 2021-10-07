package com.github.bottomlessarchive.urlcollector.validator.service;

import com.github.bottomlessarchive.urlcollector.validator.configuration.ValidationConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class URLValidator {

    private final ValidationConfigurationProperties validationConfigurationProperties;

    public boolean validateUrl(final String result) {
        if (result.isEmpty()) {
            return false;
        }

        if (!result.startsWith("http://") && !result.startsWith("https://")) {
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

        return validationConfigurationProperties.getTypes().stream()
                .anyMatch(type -> path.endsWith("." + type));
    }
}
