package com.sporthub.booking.domain.port.in;


import com.sporthub.booking.domain.model.Venue;

public interface GetVenueUseCase {
    Venue getVenueById(Long venueId);
}
