package com.sporthub.booking.infrastructure.persistence.adapter;

import com.sporthub.booking.domain.model.PagedResult;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;
import com.sporthub.booking.infrastructure.persistence.mapper.SportEventPersistenceMapper;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataSportEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SportEventPersistenceAdapter implements SportEventRepositoryPort {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;

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
    public PagedResult<SportEvent> findAll(
            Integer page,
            Integer size,
            String team,
            String city
    ) {
        int safePage = page == null || page < 0 ? DEFAULT_PAGE : page;
        int safeSize = size == null || size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);

        String teamFilter = normalizeFilter(team);
        String cityFilter = normalizeFilter(city);

        Pageable pageable = PageRequest.of(safePage, safeSize);

        Page<SportEventJpaEntity> sportEventPage = springDataSportEventRepository.findFilteredSportEvents(
                teamFilter,
                cityFilter,
                pageable
        );

        List<SportEvent> sportEvents = sportEventPage.getContent()
                .stream()
                .map(SportEventPersistenceMapper::toDomain)
                .toList();

        return new PagedResult<>(
                sportEvents,
                sportEventPage.getNumber(),
                sportEventPage.getSize(),
                sportEventPage.getTotalElements(),
                sportEventPage.getTotalPages(),
                sportEventPage.isLast()
        );
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

    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value.trim();
    }
}