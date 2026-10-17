package com.sporthub.booking.domain.port.in;

import com.sporthub.booking.domain.model.EventAvailability;

public interface GetEventAvailabilityUseCase {

    EventAvailability getEventAvailability(Long sportEventId);
}
