package com.sporthub.booking.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sport_events")

public class SportEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private TeamJpaEntity homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private TeamJpaEntity awayTeam;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private VenueJpaEntity venue;

    private LocalDateTime startTime;

    private BigDecimal ticketPrice;

    private Integer capacity;
}
