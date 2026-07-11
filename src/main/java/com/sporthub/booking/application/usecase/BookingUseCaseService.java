package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.domain.model.BookingStatus;
import com.sporthub.booking.domain.port.in.CancelBookingUseCase;
import com.sporthub.booking.domain.port.in.CreateBookingUseCase;
import com.sporthub.booking.domain.port.in.GetBookingUseCase;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import com.sporthub.booking.domain.port.out.UserRepositoryPort;
import com.sporthub.booking.domain.exception.BookingValidationException;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.model.User;

import java.util.List;
import java.time.LocalDateTime;

public class BookingUseCaseService implements CreateBookingUseCase, CancelBookingUseCase, GetBookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final SportEventRepositoryPort sportEventRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public BookingUseCaseService(
            BookingRepositoryPort bookingRepositoryPort,
            SportEventRepositoryPort sportEventRepositoryPort,
            UserRepositoryPort userRepositoryPort
    ) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.sportEventRepositoryPort = sportEventRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepositoryPort.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepositoryPort.findByUserId(userId);
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepositoryPort.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepositoryPort.save(booking);
    }

    @Override
    public Booking createBooking(Long userId, Long sportEventId, Integer numberOfTickets) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        SportEvent sportEvent = sportEventRepositoryPort.findById(sportEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Sport event not found with id: " + sportEventId));

        if (numberOfTickets == null || numberOfTickets <= 0) {
            throw new BookingValidationException("Number of tickets must be greater than zero");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSportEvent(sportEvent);
        booking.setNumberOfTickets(numberOfTickets);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.ACTIVE);

        return bookingRepositoryPort.save(booking);
    }
}