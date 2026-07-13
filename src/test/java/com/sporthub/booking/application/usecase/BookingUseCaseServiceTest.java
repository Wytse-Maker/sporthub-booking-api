package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.BookingValidationException;
import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.domain.model.BookingStatus;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.model.User;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import com.sporthub.booking.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingUseCaseServiceTest {

    @Mock
    private BookingRepositoryPort bookingRepositoryPort;

    @Mock
    private SportEventRepositoryPort sportEventRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private BookingUseCaseService bookingUseCaseService;

    @BeforeEach
    void setUp() {
        bookingUseCaseService = new BookingUseCaseService(
                bookingRepositoryPort,
                sportEventRepositoryPort,
                userRepositoryPort
        );
    }

    @Test
    void getBookingByIdReturnsBookingWhenBookingExists() {
        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingRepositoryPort.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingUseCaseService.getBookingById(1L);

        assertEquals(1L, result.getId());

    }

    @Test
    void getBookingByIdThrowsExceptionWhenBookingDoesNotExist() {
        when(bookingRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingUseCaseService.getBookingById(99L)
        );

        assertEquals("Booking not found with id: 99", exception.getMessage());
    }

    @Test
    void createBookingCreatesBookingWhenInputIsValid() {
        User user = new User();
        user.setId(1L);

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);
        sportEvent.setStartTime(LocalDateTime.now().plusDays(7));
        sportEvent.setCapacity(100);

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of());
        when(bookingRepositoryPort.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingUseCaseService.createBooking(1L, 10L, 2);

        assertEquals(user, result.getUser());
        assertEquals(sportEvent, result.getSportEvent());
        assertEquals(2, result.getNumberOfTickets());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getBookingDate());
    }

    @Test
    void createBookingThrowsExceptionWhenNumberOfTicketsIsZero() {
        User user = new User();
        user.setId(1L);

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);
        sportEvent.setStartTime(LocalDateTime.now().plusDays(7));
        sportEvent.setCapacity(100);

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));

        BookingValidationException exception = assertThrows(
                BookingValidationException.class,
                () -> bookingUseCaseService.createBooking(1L, 10L, 0)
        );

        assertEquals("Number of tickets must be greater than zero", exception.getMessage());
    }

    @Test
    void createBookingThrowsExceptionWhenNumberOfTicketsIsGreaterThanFour(){
        User user = new User();
        user.setId(1L);

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);
        sportEvent.setStartTime(LocalDateTime.now().plusDays(7));
        sportEvent.setCapacity(100);

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));

        BookingValidationException exception = assertThrows(
                BookingValidationException.class,
                () -> bookingUseCaseService.createBooking(1L, 10L, 5)
        );

        assertEquals("You can book a maximum of 4 tickets per event", exception.getMessage());
    }

    @Test
    void createBookingThrowsExceptionWhenSportEventIsInThePast() {
        User user = new User();
        user.setId(1L);

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);
        sportEvent.setStartTime(LocalDateTime.now().minusDays(1));
        sportEvent.setCapacity(100);

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));

        BookingValidationException exception = assertThrows(
                BookingValidationException.class,
                () -> bookingUseCaseService.createBooking(1L, 10L, 2)
        );

        assertEquals("Cannot book tickets for an event in the past", exception.getMessage());
    }

    @Test
    void createBookingThrowsExceptionWhenNotEnoughTicketsAreAvailable() {
        User user = new User();
        user.setId(1L);

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);
        sportEvent.setStartTime(LocalDateTime.now().plusDays(7));
        sportEvent.setCapacity(5);

        Booking existingBooking = new Booking();
        existingBooking.setNumberOfTickets(4);

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of(existingBooking));

        BookingValidationException exception = assertThrows(
                BookingValidationException.class,
                () -> bookingUseCaseService.createBooking(1L, 10L, 2)
        );

        assertEquals("Not enough available tickets for this event", exception.getMessage());
    }

    @Test
    void cancelBookingSetsStatusToCancelledWhenBookingExists() {
        SportEvent sportEvent = new SportEvent();
        sportEvent.setStartTime(LocalDateTime.now().plusDays(7));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSportEvent(sportEvent);
        booking.setStatus(BookingStatus.ACTIVE);

        when(bookingRepositoryPort.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepositoryPort.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingUseCaseService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELLED, result.getStatus());
    }

    @Test
    void cancelBookingThrowsExceptionWhenEventStartsWithin24Hours() {
        SportEvent sportEvent = new SportEvent();
        sportEvent.setStartTime(LocalDateTime.now().plusHours(12));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSportEvent(sportEvent);
        booking.setStatus(BookingStatus.ACTIVE);

        when(bookingRepositoryPort.findById(1L)).thenReturn(Optional.of(booking));

        BookingValidationException exception = assertThrows(
                BookingValidationException.class,
                () -> bookingUseCaseService.cancelBooking(1L)
        );

        assertEquals("Booking can only be cancelled up to 24 hours before the event", exception.getMessage());
    }

    @Test
    void cancelBookingThrowsExceptionWhenBookingDoesNotExist() {
        when(bookingRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingUseCaseService.cancelBooking(99L)
        );

        assertEquals("Booking not found with id: 99", exception.getMessage());
    }
}
