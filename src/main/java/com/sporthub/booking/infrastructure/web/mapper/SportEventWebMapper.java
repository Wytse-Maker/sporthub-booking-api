package com.sporthub.booking.infrastructure.web.mapper;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.infrastructure.web.dto.SportEventResponse;

public final class SportEventWebMapper {

    private SportEventWebMapper() {
    }

    public static SportEventResponse toResponse(SportEvent sportEvent) {
        return new SportEventResponse(
                sportEvent.getId(),
                sportEvent.getHomeTeam().getName(),
                sportEvent.getAwayTeam().getName(),
                sportEvent.getVenue().getName(),
                sportEvent.getStartTime(),
                sportEvent.getTicketPrice(),
                sportEvent.getCapacity()
        );
    }
}
