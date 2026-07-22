package com.sporthub.booking.infrastructure.persistence.repository;
import com.sporthub.booking.infrastructure.persistence.entity.VenueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataVenueRepository extends JpaRepository<VenueJpaEntity, Long> {
}
