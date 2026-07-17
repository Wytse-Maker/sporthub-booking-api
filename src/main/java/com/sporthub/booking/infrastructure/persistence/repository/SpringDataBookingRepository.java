package com.sporthub.booking.infrastructure.persistence.repository;
import com.sporthub.booking.domain.model.BookingStatus;
import com.sporthub.booking.infrastructure.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, Long> {

    List<BookingJpaEntity> findByUserId(Long userId);

    List<BookingJpaEntity> findBySportEventIdAndStatus(Long sportEventId, BookingStatus status);
}
