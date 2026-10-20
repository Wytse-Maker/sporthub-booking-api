package com.sporthub.booking.domain.model;

public class EventAvailability {
    private final Long sportEventId;
    private final int capacity;
    private final int reservedTickets;
    private final int availableTickets;
    private final boolean soldOut;
    private final int occupancyPercentage;


    public EventAvailability(Long sportEventId,
                             int capacity,
                             int reservedTickets,
                             int availableTickets,
                             boolean soldOut,
                             int occupancyPercentage) {
        this.sportEventId = sportEventId;
        this.capacity = capacity;
        this.reservedTickets = reservedTickets;
        this.availableTickets = availableTickets;
        this.soldOut = soldOut;
        this.occupancyPercentage = occupancyPercentage;
    }

    public Long getSportEventId() {
        return sportEventId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getReservedTickets() {
        return reservedTickets;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public int getOccupancyPercentage() {
        return occupancyPercentage;
    }
}
