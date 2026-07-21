package com.sporthub.booking.infrastructure.web.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Sport event id is required")
        Long sportEventId,

        @NotNull(message = "Number of tickets is required")
        @Min(value = 1, message = "Number of tickets must be at least 1")
        @Max(value = 4, message = "Number of tickets cannot be greater than 4")
        Integer numberOfTickets
) {
}
