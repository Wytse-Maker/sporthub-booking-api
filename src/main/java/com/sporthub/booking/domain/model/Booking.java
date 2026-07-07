package com.sporthub.booking.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;
    private User user;
    private SportEvent sportEvent;
    private Integer numberOfTickets;
    private LocalDateTime bookingDate;
    private BookingStatus status;
}
