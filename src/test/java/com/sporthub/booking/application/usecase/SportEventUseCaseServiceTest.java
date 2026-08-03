package com.sporthub.booking.application.usecase;

import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import com.sporthub.booking.domain.model.PagedResult;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SportEventUseCaseServiceTest {
    @Mock
    private SportEventRepositoryPort sportEventRepositoryPort;

    private SportEventUseCaseService sportEventUseCaseService;

    @BeforeEach
    void setUp() {
        sportEventUseCaseService = new SportEventUseCaseService(sportEventRepositoryPort);
    }

    @Test
    void getAllSportEventsReturnsAllSportEvents() {
        SportEvent sportEvent1 = new SportEvent();
        sportEvent1.setId(1L);

        SportEvent sportEvent2 = new SportEvent();
        sportEvent2.setId(2L);

        when(sportEventRepositoryPort.findAll()).thenReturn(List.of(sportEvent1, sportEvent2));

        List<SportEvent> result = sportEventUseCaseService.getAllSportEvents();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getSportEventByIdReturnsSportEventWhenSportEventExists() {
        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(10L);

        when(sportEventRepositoryPort.findById(10L)).thenReturn(Optional.of(sportEvent));

        SportEvent result = sportEventUseCaseService.getSportEventById(10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void getSportEventByIdThrowsExceptionWhenSportEventDoesNotExist() {
        when(sportEventRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> sportEventUseCaseService.getSportEventById(99L)
        );

        assertEquals("Sport event not found with id: 99", exception.getMessage());
    }
    @Test
    void getSportEventsReturnsPagedResult() {
        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(1L);

        PagedResult<SportEvent> pagedResult = new PagedResult<>(
                List.of(sportEvent),
                0,
                10,
                1,
                1,
                true
        );

        when(sportEventRepositoryPort.findAll(0, 10, "Lakers", "Los Angeles"))
                .thenReturn(pagedResult);

        PagedResult<SportEvent> result = sportEventUseCaseService.getSportEvents(
                0,
                10,
                "Lakers",
                "Los Angeles"
        );

        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());

        verify(sportEventRepositoryPort).findAll(0, 10, "Lakers", "Los Angeles");
    }

}
