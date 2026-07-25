package com.sporthub.booking.infrastructure.web.controller;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.infrastructure.web.dto.SportEventResponse;
import com.sporthub.booking.infrastructure.web.mapper.SportEventWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
@Tag(name = "Sport Events", description = "Endpoints for viewing available NBA sport events")
@RestController
@RequestMapping("/api/sport-events")
public class SportEventController {

    private final GetSportEventsUseCase getSportEventsUseCase;

    public SportEventController(GetSportEventsUseCase getSportEventsUseCase) {
        this.getSportEventsUseCase = getSportEventsUseCase;
    }

    @Operation(
            summary = "Get all sport events",
            description = "Returns all available NBA sport events with team, venue, date, price and capacity information."
    )
    @GetMapping
    public List<SportEventResponse> getAllSportEvents() {
        return getSportEventsUseCase.getAllSportEvents()
                .stream()
                .map(SportEventWebMapper::toResponse)
                .toList();
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
