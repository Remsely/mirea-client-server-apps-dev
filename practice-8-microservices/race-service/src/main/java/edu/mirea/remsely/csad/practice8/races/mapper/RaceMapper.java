package edu.mirea.remsely.csad.practice8.races.mapper;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.races.entity.RaceEntity;
import org.springframework.stereotype.Component;

@Component
public class RaceMapper {
    public RaceDto toDto(RaceEntity entity) {
        return new RaceDto(
                entity.getId(),
                entity.getName(),
                entity.getCircuit(),
                entity.getCountry(),
                entity.getRaceDate(),
                entity.getWinnerId(),
                null, // winner будет заполнен в сервисе
                entity.getWinningTeamId(),
                null  // winningTeam будет заполнен в сервисе
        );
    }

    public RaceDto toDtoWithDetails(RaceEntity entity, DriverDto winner, TeamDto winningTeam) {
        return new RaceDto(
                entity.getId(),
                entity.getName(),
                entity.getCircuit(),
                entity.getCountry(),
                entity.getRaceDate(),
                entity.getWinnerId(),
                winner,
                entity.getWinningTeamId(),
                winningTeam
        );
    }

    public RaceEntity toEntity(RaceDto dto) {
        return new RaceEntity(
                dto.getId(),
                dto.getName(),
                dto.getCircuit(),
                dto.getCountry(),
                dto.getRaceDate(),
                dto.getWinnerId(),
                dto.getWinningTeamId()
        );
    }

    public void updateEntity(RaceEntity entity, RaceDto dto) {
        entity.setName(dto.getName());
        entity.setCircuit(dto.getCircuit());
        entity.setCountry(dto.getCountry());
        entity.setRaceDate(dto.getRaceDate());
        entity.setWinnerId(dto.getWinnerId());
        entity.setWinningTeamId(dto.getWinningTeamId());
    }
}
