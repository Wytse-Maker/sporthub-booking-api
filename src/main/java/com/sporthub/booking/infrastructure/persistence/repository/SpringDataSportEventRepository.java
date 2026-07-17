package com.sporthub.booking.infrastructure.persistence.repository;
import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSportEventRepository extends JpaRepository<SportEventJpaEntity, Long> {
}
