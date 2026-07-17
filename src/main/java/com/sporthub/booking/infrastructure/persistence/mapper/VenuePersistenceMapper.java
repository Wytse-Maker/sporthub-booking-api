package com.sporthub.booking.infrastructure.persistence.mapper;

import com.sporthub.booking.domain.model.Venue;
import com.sporthub.booking.infrastructure.persistence.entity.VenueJpaEntity;

public final class VenuePersistenceMapper {

    private VenuePersistenceMapper() {
    }

    public static Venue toDomain(VenueJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Venue venue = new Venue();
        venue.setId(entity.getId());
        venue.setName(entity.getName());
        venue.setCity(entity.getCity());
        venue.setCapacity(entity.getCapacity());

        return venue;
    }

    public static VenueJpaEntity toEntity(Venue venue) {
        if (venue == null) {
            return null;
        }

        VenueJpaEntity entity = new VenueJpaEntity();
        entity.setId(venue.getId());
        entity.setName(venue.getName());
        entity.setCity(venue.getCity());
        entity.setCapacity(venue.getCapacity());

        return entity;
    }
}
