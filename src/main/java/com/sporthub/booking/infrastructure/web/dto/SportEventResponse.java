package com.sporthub.booking.infrastructure.web.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SportEventResponse(
        Long id,
        String homeTeamName,
        String awayTeamName,
        String venueName,
        LocalDateTime startTime,
        BigDecimal ticketPrice,
        Integer capacity
) {
}
