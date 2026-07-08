package com.sporthub.booking.domain.port.out;

import com.sporthub.booking.domain.model.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {
    Booking save(Booking booking);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByUserId(Long userId);

    List<Booking> findActiveBookingsBySportEventId(Long sportEventId);
}
