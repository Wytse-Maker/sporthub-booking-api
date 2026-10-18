package com.sporthub.booking.infrastructure.web.mapper;

import com.sporthub.booking.domain.model.EventAvailability;
import com.sporthub.booking.infrastructure.web.dto.EventAvailabilityResponse;

public class EventAvailabilityWebMapper {
    private EventAvailabilityWebMapper() {
    }
    public static EventAvailabilityResponse toResponse(EventAvailability eventAvailability){
        return new EventAvailabilityResponse(eventAvailability.getSportEventId(), eventAvailability.getCapacity(),
                eventAvailability.getReservedTickets(), eventAvailability.getAvailableTickets(),
                eventAvailability.isSoldOut());
    }
}
