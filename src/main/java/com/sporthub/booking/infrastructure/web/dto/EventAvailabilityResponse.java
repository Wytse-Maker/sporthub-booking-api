package com.sporthub.booking.infrastructure.web.dto;

public record EventAvailabilityResponse(Long sportEventId,
int capacity, int reservedTickets, int availableTickets, boolean soldOut) {
}
