package com.sporthub.booking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportEvent {
    private Long id;
    private Team homeTeam;
    private Team awayTeam;
    private Venue venue;
    private LocalDateTime startTime;
    private BigDecimal ticketPrice;
    private Integer capacity;
}
