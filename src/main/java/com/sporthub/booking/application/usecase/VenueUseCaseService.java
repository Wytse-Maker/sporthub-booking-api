package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Venue;
import com.sporthub.booking.domain.port.in.GetVenueUseCase;
import com.sporthub.booking.domain.port.out.VenueRepositoryPort;

public class VenueUseCaseService implements GetVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueUseCaseService(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Venue getVenueById(Long venueId) {
        return venueRepositoryPort.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venue not found with id: " + venueId
                ));
    }
}