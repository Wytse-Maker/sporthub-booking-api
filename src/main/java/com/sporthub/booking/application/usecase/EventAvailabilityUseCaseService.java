package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.OverbookingException;
import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.domain.model.EventAvailability;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.in.GetEventAvailabilityUseCase;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class EventAvailabilityUseCaseService implements GetEventAvailabilityUseCase {
    private final SportEventRepositoryPort sportEventRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;
    private static final Logger log =
            LoggerFactory.getLogger(EventAvailabilityUseCaseService.class);

    public EventAvailabilityUseCaseService(SportEventRepositoryPort sportEventRepositoryPort, BookingRepositoryPort bookingRepositoryPort) {
        this.sportEventRepositoryPort = sportEventRepositoryPort;
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    private int calculateOccupancyPercentage(int capacity, int reservedTickets) {
        if (capacity == 0) {
            return 0;
        }

        double percentage = (double) reservedTickets / capacity * 100;

        return (int) percentage;
    }

    @Override
    public EventAvailability getEventAvailability(Long sportEventId) {

        log.info("Availability requested for eventId={}", sportEventId);
        SportEvent sportEvent = sportEventRepositoryPort.findById(sportEventId)
                .orElseThrow(() ->{
                    log.warn("Sport event not found: eventId={}", sportEventId);
                    return new  ResourceNotFoundException("Sport event not found with id: " + sportEventId);
                });

        List<Booking> activeBookings = bookingRepositoryPort.findActiveBookingsBySportEventId(sportEventId);

        log.debug("Active bookings found for eventId={}: count={}", sportEventId, activeBookings.size()) ;

        int reservedTickets = activeBookings.stream().mapToInt(Booking::getNumberOfTickets).sum();



        int capacity = sportEvent.getCapacity();

        if (reservedTickets > capacity) {
            throw new OverbookingException( "Event " + sportEventId
                    + ": reserved tickets (" + reservedTickets
                    + ") exceed capacity (" + capacity + ")");
        }
        int availableTickets = capacity - reservedTickets;

        int occupancyPercentage = calculateOccupancyPercentage(capacity, reservedTickets);

        boolean soldOut = availableTickets <= 0;


        return new EventAvailability(sportEventId, capacity, reservedTickets, availableTickets, soldOut, occupancyPercentage);
    }

}
