package com.sporthub.booking.infrastructure.web.dto;

public record VenueResponse(
        Long id,
        String name,
        String city,
        Integer capacity
) {
}