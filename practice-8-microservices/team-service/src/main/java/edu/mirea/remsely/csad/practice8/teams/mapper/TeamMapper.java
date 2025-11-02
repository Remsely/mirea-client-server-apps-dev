package edu.mirea.remsely.csad.practice8.teams.mapper;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.teams.entity.TeamEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamDto toDto(TeamEntity entity) {
        return new TeamDto(
                entity.getId(),
                entity.getName(),
                entity.getBaseLocation(),
                entity.getTeamPrincipal(),
                entity.getEngineSupplier(),
                entity.getChampionships()
        );
    }

    public TeamEntity toEntity(TeamDto dto) {
        return new TeamEntity(
                dto.getId(),
                dto.getName(),
                dto.getBaseLocation(),
                dto.getTeamPrincipal(),
                dto.getEngineSupplier(),
                dto.getChampionships()
        );
    }

    public void updateEntity(TeamEntity entity, TeamDto dto) {
        entity.setName(dto.getName());
        entity.setBaseLocation(dto.getBaseLocation());
        entity.setTeamPrincipal(dto.getTeamPrincipal());
        entity.setEngineSupplier(dto.getEngineSupplier());
        entity.setChampionships(dto.getChampionships());
    }
}

