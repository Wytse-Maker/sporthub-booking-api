package com.sporthub.booking.infrastructure.web.controller;

import com.sporthub.booking.domain.model.PagedResult;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.infrastructure.web.dto.PagedResponse;
import com.sporthub.booking.infrastructure.web.dto.SportEventResponse;
import com.sporthub.booking.infrastructure.web.mapper.SportEventWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sport Events", description = "Endpoints for viewing available NBA sport events")
@RestController
@RequestMapping("/api/sport-events")
public class SportEventController {

    private final GetSportEventsUseCase getSportEventsUseCase;

    public SportEventController(GetSportEventsUseCase getSportEventsUseCase) {
        this.getSportEventsUseCase = getSportEventsUseCase;
    }

    @Operation(
            summary = "Get sport events with pagination and filtering",
            description = "Returns NBA sport events with optional pagination and filtering by team or venue city."
    )
    @GetMapping
    public PagedResponse<SportEventResponse> getSportEvents(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String city
    ) {
        PagedResult<SportEvent> sportEventPage = getSportEventsUseCase.getSportEvents(
                page,
                size,
                team,
                city
        );

        return new PagedResponse<>(
                sportEventPage.getContent()
                        .stream()
                        .map(SportEventWebMapper::toResponse)
                        .toList(),
                sportEventPage.getPage(),
                sportEventPage.getSize(),
                sportEventPage.getTotalElements(),
                sportEventPage.getTotalPages(),
                sportEventPage.isLast()
        );
    }

    @Operation(
            summary = "Get sport event by ID",
            description = "Returns a single sport event by its ID."
    )
    @GetMapping("/{sportEventId}")
    public SportEventResponse getSportEventById(@PathVariable Long sportEventId) {
        return SportEventWebMapper.toResponse(
                getSportEventsUseCase.getSportEventById(sportEventId)
        );
    }
}