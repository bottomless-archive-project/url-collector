package com.github.bottomlessarchive.urlcollector.workunit.view.work;

import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnit;
import com.github.bottomlessarchive.urlcollector.workunit.service.domain.WorkUnitStatus;
import com.github.bottomlessarchive.urlcollector.workunit.service.work.WorkUnitFactory;
import com.github.bottomlessarchive.urlcollector.workunit.service.work.WorkUnitService;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.FinishWorkUnitRequest;
import com.github.bottomlessarchive.urlcollector.workunit.view.response.StartWorkUnitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/work-unit")
@RequiredArgsConstructor
public class WorkUnitController {

    private final WorkUnitService workUnitService;
    private final WorkUnitFactory workUnitFactory;

    @PostMapping("/start-work")
    public StartWorkUnitResponse startWorkUnit() {
        final WorkUnit workUnit = workUnitService.startWorkUnit()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "No more work units available!"));

        return StartWorkUnitResponse.builder()
                .id(workUnit.getId().toString())
                .location(workUnit.getLocation())
                .build();
    }

    @PostMapping("/finish-work")
    public void finishWorkUnit(@RequestBody final FinishWorkUnitRequest finishWorkUnitRequest) {
        final WorkUnit workUnit = workUnitFactory.getWorkUnit(UUID.fromString(finishWorkUnitRequest.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown work unit!"));

        if (!WorkUnitStatus.UNDER_PROCESSING.equals(workUnit.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Work unit is not under processing!");
        }

        workUnitService.finishWorkUnit(workUnit);
    }
}
