package com.sporthub.booking.domain.port.out;


import com.sporthub.booking.domain.model.Venue;


import java.util.Optional;

public interface VenueRepositoryPort {
    Optional<Venue> findById(Long venueId);

}
