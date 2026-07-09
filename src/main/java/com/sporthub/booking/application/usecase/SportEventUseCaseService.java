package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import java.util.List;

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
}

