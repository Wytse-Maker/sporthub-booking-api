package com.sporthub.booking.infrastructure.persistence.repository;

import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataSportEventRepository extends JpaRepository<SportEventJpaEntity, Long> {

    @Query("""
            SELECT sportEvent
            FROM SportEventJpaEntity sportEvent
            WHERE (:team = ''
                OR LOWER(sportEvent.homeTeam.name) LIKE LOWER(CONCAT('%', :team, '%'))
                OR LOWER(sportEvent.awayTeam.name) LIKE LOWER(CONCAT('%', :team, '%')))
            AND (:city = ''
                OR LOWER(sportEvent.venue.city) LIKE LOWER(CONCAT('%', :city, '%')))
            ORDER BY sportEvent.startTime ASC
            """)
    Page<SportEventJpaEntity> findFilteredSportEvents(
            @Param("team") String team,
            @Param("city") String city,
            Pageable pageable
    );
}