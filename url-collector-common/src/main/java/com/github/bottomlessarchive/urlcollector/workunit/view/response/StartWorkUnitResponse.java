package com.github.bottomlessarchive.urlcollector.workunit.view.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Builder
@Jacksonized
public class StartWorkUnitResponse {

    private final String id;
    private final Set<String> locations;
}
