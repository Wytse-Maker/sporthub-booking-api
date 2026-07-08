package com.sporthub.booking.domain.port.out;

import com.sporthub.booking.domain.model.SportEvent;
import java.util.List;
import java.util.Optional;

public interface SportEventRepositoryPort {

    Optional<SportEvent> findById(Long sportEventId);

    List<SportEvent> findAll();

    SportEvent save(SportEvent sportEvent);

    void deleteById(Long sportEventId);

}
