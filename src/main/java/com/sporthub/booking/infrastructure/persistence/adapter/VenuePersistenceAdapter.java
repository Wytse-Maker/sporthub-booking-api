package com.sporthub.booking.infrastructure.persistence.adapter;

import com.sporthub.booking.domain.model.Venue;
import com.sporthub.booking.domain.port.out.VenueRepositoryPort;
import com.sporthub.booking.infrastructure.persistence.mapper.VenuePersistenceMapper;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataVenueRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VenuePersistenceAdapter implements VenueRepositoryPort {

    private final SpringDataVenueRepository springDataVenueRepository;

    public VenuePersistenceAdapter(SpringDataVenueRepository springDataVenueRepository) {
        this.springDataVenueRepository = springDataVenueRepository;
    }

    @Override
    public Optional<Venue> findById(Long venueId) {
        return springDataVenueRepository.findById(venueId)
                .map(VenuePersistenceMapper::toDomain);
    }
}