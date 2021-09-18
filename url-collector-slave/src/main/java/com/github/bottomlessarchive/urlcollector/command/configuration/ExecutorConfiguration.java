package com.github.bottomlessarchive.urlcollector.command.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Configuration
public class ExecutorConfiguration {

    @Bean
    public ExecutorService commandExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Bean
    public Semaphore commandRateLimitingSemaphore() {
        return new Semaphore(Runtime.getRuntime().availableProcessors());
    }
}
