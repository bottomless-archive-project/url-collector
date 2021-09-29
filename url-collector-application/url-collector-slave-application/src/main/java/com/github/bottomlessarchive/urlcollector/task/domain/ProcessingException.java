package com.github.bottomlessarchive.urlcollector.task.domain;

public class ProcessingException extends RuntimeException {

    public ProcessingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
