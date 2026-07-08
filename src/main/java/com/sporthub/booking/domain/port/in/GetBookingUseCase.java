package com.sporthub.booking.domain.port.in;

import com.sporthub.booking.domain.model.Booking;
import java.util.List;

public interface GetBookingUseCase {
    Booking getBookingById(Long bookingId);

    List<Booking> getBookingsByUserId(Long userId);
}
