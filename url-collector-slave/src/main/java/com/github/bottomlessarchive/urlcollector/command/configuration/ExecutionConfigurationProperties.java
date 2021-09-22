package com.github.bottomlessarchive.urlcollector.command.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("execution")
public class ExecutionConfigurationProperties {

    private int parallelismTarget = 0;

    public int getParallelismTarget() {
        if (parallelismTarget == 0) {
            return Runtime.getRuntime().availableProcessors() * 2;
        }

        return parallelismTarget;
    }
}
