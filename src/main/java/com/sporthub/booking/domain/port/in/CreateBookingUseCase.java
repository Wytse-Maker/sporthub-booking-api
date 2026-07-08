package com.sporthub.booking.domain.port.in;
import com.sporthub.booking.domain.model.Booking;

public interface CreateBookingUseCase {
    Booking createBooking(Long userId, Long sportEventId, Integer numberOfTickets);
}
