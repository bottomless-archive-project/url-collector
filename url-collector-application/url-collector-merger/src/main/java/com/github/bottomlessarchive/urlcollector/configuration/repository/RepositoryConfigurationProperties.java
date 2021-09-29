package com.github.bottomlessarchive.urlcollector.configuration.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("urls.database")
public class RepositoryConfigurationProperties {

    private final String host;
    private final int port;
    private final String uri;

    public boolean isUriConfiguration() {
        return uri != null && !uri.isEmpty();
    }
}
