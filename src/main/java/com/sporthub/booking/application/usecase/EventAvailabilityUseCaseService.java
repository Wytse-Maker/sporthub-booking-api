package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.domain.model.EventAvailability;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.in.GetEventAvailabilityUseCase;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;

import java.util.List;





public class EventAvailabilityUseCaseService implements GetEventAvailabilityUseCase {
    private final SportEventRepositoryPort sportEventRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;

    public EventAvailabilityUseCaseService(SportEventRepositoryPort sportEventRepositoryPort, BookingRepositoryPort bookingRepositoryPort) {
        this.sportEventRepositoryPort = sportEventRepositoryPort;
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    @Override
    public EventAvailability getEventAvailability(Long sportEventId) {
        SportEvent sportEvent = sportEventRepositoryPort.findById(sportEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Sport event not found with id: " + sportEventId));

        List<Booking>activeBookings=bookingRepositoryPort.findActiveBookingsBySportEventId(sportEventId);

        int reservedTickets=activeBookings.stream().mapToInt(Booking::getNumberOfTickets).sum();

        int capacity=sportEvent.getCapacity();
        int availableTickets=capacity-reservedTickets;
        boolean soldOut = availableTickets <= 0;

        return new EventAvailability(sportEventId,capacity,reservedTickets,availableTickets,soldOut);
    }

}
