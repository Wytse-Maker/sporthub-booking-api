package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.Venue;
import com.sporthub.booking.domain.port.out.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VenueUseCaseServiceTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    private VenueUseCaseService venueUseCaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        venueUseCaseService = new VenueUseCaseService(
                venueRepositoryPort
        );
    }

    @Test
    void getVenueByIdReturnsVenueWhenVenueExists() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("Crypto.com Arena");
        venue.setCity("Los Angeles");
        venue.setCapacity(20000);

        when(venueRepositoryPort.findById(1L))
                .thenReturn(Optional.of(venue));

        Venue result = venueUseCaseService.getVenueById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Crypto.com Arena", result.getName());
        assertEquals("Los Angeles", result.getCity());
        assertEquals(20000, result.getCapacity());

        verify(venueRepositoryPort).findById(1L);
    }

    @Test
    void getVenueByIdThrowsExceptionWhenVenueDoesNotExist() {
        when(venueRepositoryPort.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> venueUseCaseService.getVenueById(999L)
        );

        assertEquals(
                "Venue not found with id: 999",
                exception.getMessage()
        );

        verify(venueRepositoryPort).findById(999L);
    }
}