package com.sporthub.booking.infrastructure.web.mapper;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.infrastructure.web.dto.BookingResponse;

public final class BookingWebMapper {

    private BookingWebMapper() {
    }

    public static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getSportEvent().getId(),
                booking.getNumberOfTickets(),
                booking.getBookingDate(),
                booking.getStatus()
        );
    }
}
