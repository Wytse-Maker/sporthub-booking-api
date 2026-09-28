package com.sporthub.booking.infrastructure.web.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SportEventResponse(
        Long id,
        Long homeTeamId,
        String homeTeamName,
        Long awayTeamId,
        String awayTeamName,
        Long venueId,
        String venueName,
        LocalDateTime startTime,
        BigDecimal ticketPrice,
        Integer capacity
) {
}
