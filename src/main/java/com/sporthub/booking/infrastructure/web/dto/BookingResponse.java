package com.sporthub.booking.infrastructure.web.dto;

import com.sporthub.booking.domain.model.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        Long sportEventId,
        Integer numberOfTickets,
        LocalDateTime bookingDate,
        BookingStatus status
) {
}
