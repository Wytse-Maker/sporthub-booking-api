package com.sporthub.booking.infrastructure.web.controller;

import com.sporthub.booking.domain.exception.OverbookingException;
import com.sporthub.booking.domain.exception.ResourceNotFoundException;
import com.sporthub.booking.domain.model.EventAvailability;
import com.sporthub.booking.domain.port.in.GetEventAvailabilityUseCase;
import com.sporthub.booking.domain.port.in.GetSportEventsUseCase;
import com.sporthub.booking.infrastructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportEventController.class)
@Import(GlobalExceptionHandler.class)
class SportEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetSportEventsUseCase getSportEventsUseCase;


    @MockitoBean
    private GetEventAvailabilityUseCase getEventAvailabilityUseCase;

    @Test
    void shouldReturn409WhenEventIsOverbooked() throws Exception {
        when(getEventAvailabilityUseCase.getEventAvailability(10L))
                .thenThrow(new OverbookingException("Event is overbooked"));


        mockMvc.perform(
                        get("/api/sport-events/{sportEventId}/availability", 10L)
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Event is overbooked"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.timestamp").exists());


    }

    @Test
    void shouldReturn404WhenEventDoesNotExist() throws Exception {
        when(getEventAvailabilityUseCase.getEventAvailability((999L)))
                .thenThrow(new ResourceNotFoundException("Sport event not found with id: 999"));

        mockMvc.perform(
                        get("/api/sport-events/{sportEventId}/availability", 999L)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Sport event not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn200WhenEventAvailabilityExists() throws Exception {
        EventAvailability availability =
                new EventAvailability(10L,
                        20,
                        7,
                        13,
                        false,
                        35);
        when(getEventAvailabilityUseCase.getEventAvailability(10L))
                .thenReturn(availability);
        mockMvc.perform(
                        get("/api/sport-events/{sportEventId}/availability", 10L)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.sportEventId").value(10L))
                .andExpect(jsonPath("$.capacity").value(20))
                .andExpect(jsonPath("$.reservedTickets").value(7))
                .andExpect(jsonPath("$.availableTickets").value(13))
                .andExpect(jsonPath("$.soldOut").value(false))
                .andExpect(jsonPath("$.occupancyPercentage").value(35));

    }

    @Test
    void shouldReturn404WhenSportEventByIdDoesNotExist() throws Exception {
        when(getSportEventsUseCase.getSportEventById(999L))
                .thenThrow(new ResourceNotFoundException("Sport event not found with id: 999"));

        mockMvc.perform(get("/api/sport-events/{sportEventId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Sport event not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());

    }
}