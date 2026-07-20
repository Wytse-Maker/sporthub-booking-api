package com.sporthub.booking.infrastructure.web.dto;

public record CreateBookingRequest(
        Long userId,
        Long sportEventId,
        Integer numberOfTickets
) {
}
