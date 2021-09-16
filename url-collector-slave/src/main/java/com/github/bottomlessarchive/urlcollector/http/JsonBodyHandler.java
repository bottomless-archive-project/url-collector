package com.github.bottomlessarchive.urlcollector.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class JsonBodyHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <W> HttpResponse.BodyHandler<Supplier<W>> jsonBodyHandler(final Class<W> wClass) {
        return responseInfo -> asJSON(wClass);
    }

    @SneakyThrows
    public static HttpRequest.BodyPublisher jsonBodyPublisher(final Object obj) {
        final String body = OBJECT_MAPPER.writeValueAsString(obj);

        return HttpRequest.BodyPublishers.ofString(body);
    }

    private static <W> HttpResponse.BodySubscriber<Supplier<W>> asJSON(Class<W> targetType) {
        final HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream, inputStream -> toSupplierOfType(inputStream, targetType));
    }

    private static <W> Supplier<W> toSupplierOfType(InputStream inputStream, Class<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                return OBJECT_MAPPER.readValue(stream, targetType);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}
