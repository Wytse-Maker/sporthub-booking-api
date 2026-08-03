package com.sporthub.booking.domain.port.in;

import com.sporthub.booking.domain.model.PagedResult;
import com.sporthub.booking.domain.model.SportEvent;

import java.util.List;

public interface GetSportEventsUseCase {

    List<SportEvent> getAllSportEvents();

    SportEvent getSportEventById(Long sportEventId);

    PagedResult<SportEvent> getSportEvents(
            Integer page,
            Integer size,
            String team,
            String city
    );
}