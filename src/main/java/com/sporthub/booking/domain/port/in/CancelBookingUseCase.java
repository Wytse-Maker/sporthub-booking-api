package com.sporthub.booking.domain.port.in;

import com.sporthub.booking.domain.model.Booking;

public interface CancelBookingUseCase {
    Booking cancelBooking(Long bookingId);
}
