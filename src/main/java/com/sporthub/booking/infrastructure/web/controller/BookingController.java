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

import java.util.List;

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

    @GetMapping("/{bookingId}")
    public BookingResponse getBookingById(@PathVariable Long bookingId) {
        return BookingWebMapper.toResponse(
                getBookingUseCase.getBookingById(bookingId)
        );
    }

    @GetMapping("/users/{userId}")
    public List<BookingResponse> getBookingsByUserId(@PathVariable Long userId) {
        return getBookingUseCase.getBookingsByUserId(userId)
                .stream()
                .map(BookingWebMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{bookingId}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long bookingId) {
        return BookingWebMapper.toResponse(
                cancelBookingUseCase.cancelBooking(bookingId)
        );
    }
}
