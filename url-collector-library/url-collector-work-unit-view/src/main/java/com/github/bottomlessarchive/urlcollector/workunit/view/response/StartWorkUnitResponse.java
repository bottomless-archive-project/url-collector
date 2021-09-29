package com.github.bottomlessarchive.urlcollector.workunit.view.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class StartWorkUnitResponse {

    private final String id;
    private final String location;
}
