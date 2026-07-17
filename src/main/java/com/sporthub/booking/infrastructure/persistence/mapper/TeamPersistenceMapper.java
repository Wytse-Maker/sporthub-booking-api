package com.sporthub.booking.infrastructure.persistence.mapper;
import com.sporthub.booking.domain.model.Team;
import com.sporthub.booking.infrastructure.persistence.entity.TeamJpaEntity;

public final class TeamPersistenceMapper {

    private TeamPersistenceMapper() {
    }

    public static Team toDomain(TeamJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Team team = new Team();
        team.setId(entity.getId());
        team.setName(entity.getName());
        team.setCity(entity.getCity());
        team.setAbbreviation(entity.getAbbreviation());

        return team;
    }

    public static TeamJpaEntity toEntity(Team team) {
        if (team == null) {
            return null;
        }

        TeamJpaEntity entity = new TeamJpaEntity();
        entity.setId(team.getId());
        entity.setName(team.getName());
        entity.setCity(team.getCity());
        entity.setAbbreviation(team.getAbbreviation());

        return entity;
    }
}
