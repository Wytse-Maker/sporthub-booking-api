package com.sporthub.booking.infrastructure.persistence.repository;

import com.sporthub.booking.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
}
