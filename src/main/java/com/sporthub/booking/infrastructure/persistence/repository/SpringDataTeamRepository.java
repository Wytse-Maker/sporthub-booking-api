package com.sporthub.booking.infrastructure.persistence.repository;
import com.sporthub.booking.infrastructure.persistence.entity.TeamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTeamRepository extends JpaRepository<TeamJpaEntity, Long> {
}
