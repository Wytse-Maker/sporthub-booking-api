package com.sporthub.booking.infrastructure.persistence.adapter;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import com.sporthub.booking.infrastructure.persistence.mapper.SportEventPersistenceMapper;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataSportEventRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SportEventPersistenceAdapter implements SportEventRepositoryPort {

    private final SpringDataSportEventRepository springDataSportEventRepository;

    public SportEventPersistenceAdapter(SpringDataSportEventRepository springDataSportEventRepository) {
        this.springDataSportEventRepository = springDataSportEventRepository;
    }

    @Override
    public Optional<SportEvent> findById(Long sportEventId) {
        return springDataSportEventRepository.findById(sportEventId)
                .map(SportEventPersistenceMapper::toDomain);
    }

    @Override
    public List<SportEvent> findAll() {
        return springDataSportEventRepository.findAll()
                .stream()
                .map(SportEventPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public SportEvent save(SportEvent sportEvent) {
        return SportEventPersistenceMapper.toDomain(
                springDataSportEventRepository.save(SportEventPersistenceMapper.toEntity(sportEvent))
        );
    }

    @Override
    public void deleteById(Long sportEventId) {
        springDataSportEventRepository.deleteById(sportEventId);
    }
}
