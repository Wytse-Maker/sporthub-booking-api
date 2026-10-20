package com.sporthub.booking.application.usecase;


import com.sporthub.booking.domain.exception.BookingValidationException;
import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventAvailabilityUseCaseServiceTest {
    @Mock
    private BookingRepositoryPort bookingRepositoryPort;

    @Mock
    private SportEventRepositoryPort sportEventRepositoryPort;

    private EventAvailabilityUseCaseService eventAvailabilityUseCaseService;

    @BeforeEach
    void setUp() {
        eventAvailabilityUseCaseService = new EventAvailabilityUseCaseService(sportEventRepositoryPort,

                bookingRepositoryPort);

    }

    @Test
    void shouldCalculateAvailableTicketsCorrectly() {
        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(100);
        sportEvent.setId(10L);
        Booking booking1=new Booking();
        booking1.setNumberOfTickets(2);
        Booking booking2= new Booking();
        booking2.setNumberOfTickets(3);
        Booking booking3=new Booking();
        booking3.setNumberOfTickets(4);

        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of(booking1,booking2,booking3));

       EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

       assertEquals(10L,result.getSportEventId());
       assertEquals(100, result.getCapacity());
       assertEquals(9, result.getReservedTickets());
       assertEquals(91, result.getAvailableTickets());
       assertFalse(result.isSoldOut());

    }

    @Test
    void shouldReturnFullAvailabilityWhenNoBookingsExist() {
        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(100);
        sportEvent.setId(10L);

        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of());

        EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

        assertEquals(10L,result.getSportEventId());
        assertEquals(100, result.getCapacity());
        assertEquals(0, result.getReservedTickets());
        assertEquals(100, result.getAvailableTickets());
        assertFalse(result.isSoldOut());

    }

    @Test
    void shouldThrowExceptionWhenSportEventDoesNotExist() {

        ;

        when(sportEventRepositoryPort.findById(999L)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            eventAvailabilityUseCaseService.getEventAvailability(999L);
        });
    }

    @Test
    void shouldReturnSoldOutWhenAllTicketsAreReserved() {

        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(10);
        sportEvent.setId(10L);
        Booking booking1=new Booking();
        booking1.setNumberOfTickets(4);
        Booking booking2= new Booking();
        booking2.setNumberOfTickets(4);
        Booking booking3=new Booking();
        booking3.setNumberOfTickets(2);


        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of(booking1,booking2, booking3));

        EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

        assertEquals(10L,result.getSportEventId());
        assertEquals(10, result.getCapacity());
        assertEquals(10, result.getReservedTickets());
        assertEquals(0, result.getAvailableTickets());
        assertTrue(result.isSoldOut());

    }

    @Test
    void shouldCalculateOccupancyPercentageCorrectly(){
        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(20);
        sportEvent.setId(10L);
        Booking booking1=new Booking();
        booking1.setNumberOfTickets(7);

        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of(booking1));

        EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

        assertEquals(20, result.getCapacity());
        assertEquals(7, result.getReservedTickets());
        assertEquals(35, result.getOccupancyPercentage());
    }

    @Test
    void shouldCalculateOccupancyPercentageCorrectlyWithZeroCapacityAndZeroReservedTickets(){
        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(0);
        sportEvent.setId(10L);


        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of());

        EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

        assertEquals(0, result.getCapacity());
        assertEquals(0, result.getReservedTickets());
        assertEquals(0, result.getOccupancyPercentage());
    }
    @Test
    void shouldRoundDownOccupancyPercentage(){
        SportEvent sportEvent=new SportEvent();
        sportEvent.setCapacity(3);
        sportEvent.setId(10L);
        Booking booking1=new Booking();
        booking1.setNumberOfTickets(1);

        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));
        when(bookingRepositoryPort.findActiveBookingsBySportEventId(10L)).thenReturn(List.of(booking1));

        EventAvailability result= eventAvailabilityUseCaseService.getEventAvailability(10L);

        assertEquals(3, result.getCapacity());
        assertEquals(1, result.getReservedTickets());
        assertEquals(33, result.getOccupancyPercentage());
    }
    }
