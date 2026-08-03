package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import java.util.List;
import com.sporthub.booking.domain.model.PagedResult;

public class SportEventUseCaseService implements GetSportEventsUseCase {
    private final SportEventRepositoryPort sportEventRepositoryPort;

    public SportEventUseCaseService(SportEventRepositoryPort sportEventRepositoryPort) {
        this.sportEventRepositoryPort = sportEventRepositoryPort;
    }

    @Override
    public List<SportEvent> getAllSportEvents() {
        return sportEventRepositoryPort.findAll();
    }

    @Override
    public SportEvent getSportEventById(Long sportEventId) {
        return sportEventRepositoryPort.findById(sportEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Sport event not found with id: " + sportEventId));
    }
    @Override
    public PagedResult<SportEvent> getSportEvents(
            Integer page,
            Integer size,
            String team,
            String city
    ) {
        return sportEventRepositoryPort.findAll(page, size, team, city);
    }
}

