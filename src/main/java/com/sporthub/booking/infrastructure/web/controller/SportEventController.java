package com.sporthub.booking.infrastructure.web.controller;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.infrastructure.web.dto.SportEventResponse;
import com.sporthub.booking.infrastructure.web.mapper.SportEventWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sport-events")
public class SportEventController {

    private final GetSportEventsUseCase getSportEventsUseCase;

    public SportEventController(GetSportEventsUseCase getSportEventsUseCase) {
        this.getSportEventsUseCase = getSportEventsUseCase;
    }

    @GetMapping
    public List<SportEventResponse> getAllSportEvents() {
        return getSportEventsUseCase.getAllSportEvents()
                .stream()
                .map(SportEventWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{sportEventId}")
    public SportEventResponse getSportEventById(@PathVariable Long sportEventId) {
        return SportEventWebMapper.toResponse(
                getSportEventsUseCase.getSportEventById(sportEventId)
        );
    }
}
