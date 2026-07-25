package com.sporthub.booking.infrastructure.web.controller;
import com.sporthub.booking.domain.port.in.CancelBookingUseCase;
import com.sporthub.booking.domain.port.in.CreateBookingUseCase;
import com.sporthub.booking.domain.port.in.GetBookingUseCase;
import com.sporthub.booking.infrastructure.web.dto.BookingResponse;
import com.sporthub.booking.infrastructure.web.dto.CreateBookingRequest;
import com.sporthub.booking.infrastructure.web.mapper.BookingWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Bookings", description = "Endpoints for creating, retrieving and cancelling bookings")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;

    public BookingController(
            CreateBookingUseCase createBookingUseCase,
            CancelBookingUseCase cancelBookingUseCase,
            GetBookingUseCase getBookingUseCase
    ) {
        this.createBookingUseCase = createBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.getBookingUseCase = getBookingUseCase;
    }

    @Operation(
            summary = "Create a booking",
            description = "Creates a new booking for a user and sport event. The number of tickets must be between 1 and 4."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return BookingWebMapper.toResponse(
                createBookingUseCase.createBooking(
                        request.userId(),
                        request.sportEventId(),
                        request.numberOfTickets()
                )
        );
    }

    @Operation(
            summary = "Get booking by ID",
            description = "Returns a booking by its ID."
    )
    @GetMapping("/{bookingId}")
    public BookingResponse getBookingById(@PathVariable Long bookingId) {
        return BookingWebMapper.toResponse(
                getBookingUseCase.getBookingById(bookingId)
        );
    }

    @Operation(
            summary = "Get bookings by user ID",
            description = "Returns all bookings linked to a specific user."
    )
    @GetMapping("/users/{userId}")
    public List<BookingResponse> getBookingsByUserId(@PathVariable Long userId) {
        return getBookingUseCase.getBookingsByUserId(userId)
                .stream()
                .map(BookingWebMapper::toResponse)
                .toList();
    }

    @Operation(
            summary = "Cancel booking",
            description = "Cancels an existing booking if the event starts more than 24 hours in the future."
    )
    @PatchMapping("/{bookingId}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long bookingId) {
        return BookingWebMapper.toResponse(
                cancelBookingUseCase.cancelBooking(bookingId)
        );
    }
}
