package com.sporthub.booking.infrastructure.web.mapper;

import com.sporthub.booking.domain.model.Venue;
import com.sporthub.booking.infrastructure.web.dto.VenueResponse;

public final class VenueWebMapper {
    private VenueWebMapper() {

    }

    public static VenueResponse toResponse(Venue venue) {
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getCapacity()
        );
    }
}
