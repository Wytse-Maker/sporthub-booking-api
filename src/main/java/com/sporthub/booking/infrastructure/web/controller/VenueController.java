package com.sporthub.booking.infrastructure.web.controller;

import com.sporthub.booking.domain.port.in.GetVenueUseCase;
import com.sporthub.booking.infrastructure.web.dto.VenueResponse;
import com.sporthub.booking.infrastructure.web.mapper.VenueWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Venues",
        description = "Endpoints for viewing NBA venues"
)
@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final GetVenueUseCase getVenueUseCase;

    public VenueController(GetVenueUseCase getVenueUseCase) {
        this.getVenueUseCase = getVenueUseCase;
    }

    @Operation(
            summary = "Get venue by ID",
            description = "Returns a single venue by its ID."
    )
    @GetMapping("/{venueId}")
    public VenueResponse getVenueById(@PathVariable Long venueId) {
        return VenueWebMapper.toResponse(
                getVenueUseCase.getVenueById(venueId)
        );
    }
}