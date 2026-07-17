package com.sporthub.booking.infrastructure.persistence.mapper;
import com.sporthub.booking.domain.model.SportEvent;
import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;

public final class SportEventPersistenceMapper {

    private SportEventPersistenceMapper() {
    }

    public static SportEvent toDomain(SportEventJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        SportEvent sportEvent = new SportEvent();
        sportEvent.setId(entity.getId());
        sportEvent.setHomeTeam(TeamPersistenceMapper.toDomain(entity.getHomeTeam()));
        sportEvent.setAwayTeam(TeamPersistenceMapper.toDomain(entity.getAwayTeam()));
        sportEvent.setVenue(VenuePersistenceMapper.toDomain(entity.getVenue()));
        sportEvent.setStartTime(entity.getStartTime());
        sportEvent.setTicketPrice(entity.getTicketPrice());
        sportEvent.setCapacity(entity.getCapacity());

        return sportEvent;
    }

    public static SportEventJpaEntity toEntity(SportEvent sportEvent) {
        if (sportEvent == null) {
            return null;
        }

        SportEventJpaEntity entity = new SportEventJpaEntity();
        entity.setId(sportEvent.getId());
        entity.setHomeTeam(TeamPersistenceMapper.toEntity(sportEvent.getHomeTeam()));
        entity.setAwayTeam(TeamPersistenceMapper.toEntity(sportEvent.getAwayTeam()));
        entity.setVenue(VenuePersistenceMapper.toEntity(sportEvent.getVenue()));
        entity.setStartTime(sportEvent.getStartTime());
        entity.setTicketPrice(sportEvent.getTicketPrice());
        entity.setCapacity(sportEvent.getCapacity());

        return entity;
    }
}
