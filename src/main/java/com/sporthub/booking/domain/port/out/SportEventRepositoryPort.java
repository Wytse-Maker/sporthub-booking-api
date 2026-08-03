package com.sporthub.booking.domain.port.out;

import com.sporthub.booking.domain.model.PagedResult;
import com.sporthub.booking.domain.model.SportEvent;

import java.util.List;
import java.util.Optional;

public interface SportEventRepositoryPort {

    Optional<SportEvent> findById(Long sportEventId);

    List<SportEvent> findAll();

    PagedResult<SportEvent> findAll(
            Integer page,
            Integer size,
            String team,
            String city
    );

    SportEvent save(SportEvent sportEvent);

    void deleteById(Long sportEventId);
}