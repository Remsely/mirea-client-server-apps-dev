package edu.mirea.remsely.csad.practice8.races.mapper;

import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.races.entity.RaceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RaceMapperTest {

    private RaceMapper raceMapper;

    @BeforeEach
    void setUp() {
        raceMapper = new RaceMapper();
    }

    @Test
    void toDto_ShouldConvertEntityToDto() {
        // Arrange
        RaceEntity entity = new RaceEntity(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                3L
        );

        // Act
        RaceDto dto = raceMapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getCircuit(), dto.getCircuit());
        assertEquals(entity.getCountry(), dto.getCountry());
        assertEquals(entity.getRaceDate(), dto.getRaceDate());
        assertEquals(entity.getWinnerId(), dto.getWinnerId());
        assertEquals(entity.getWinningTeamId(), dto.getWinningTeamId());
        assertNull(dto.getWinner());
        assertNull(dto.getWinningTeam());
    }

    @Test
    void toDtoWithDetails_ShouldIncludeWinnerAndTeamObjects() {
        // Arrange
        RaceEntity entity = new RaceEntity(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                3L
        );

        edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto driver =
                new edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto(
                        5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto team =
                new edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto(
                        3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        // Act
        RaceDto dto = raceMapper.toDtoWithDetails(entity, driver, team);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getWinner());
        assertEquals("Charles", dto.getWinner().getFirstName());
        assertEquals("Leclerc", dto.getWinner().getLastName());
        assertNotNull(dto.getWinningTeam());
        assertEquals("Ferrari", dto.getWinningTeam().getName());
    }

    @Test
    void toEntity_ShouldConvertDtoToEntity() {
        // Arrange
        edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto driver =
                new edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto(
                        5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto team =
                new edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto(
                        3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        RaceDto dto = new RaceDto(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                driver,
                3L,
                team
        );

        // Act
        RaceEntity entity = raceMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCircuit(), entity.getCircuit());
        assertEquals(dto.getCountry(), entity.getCountry());
        assertEquals(dto.getRaceDate(), entity.getRaceDate());
        assertEquals(dto.getWinnerId(), entity.getWinnerId());
        assertEquals(dto.getWinningTeamId(), entity.getWinningTeamId());
    }

    @Test
    void updateEntity_ShouldUpdateExistingEntity() {
        // Arrange
        RaceEntity entity = new RaceEntity(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                null,
                null
        );

        edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto driver =
                new edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto(
                        5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto team =
                new edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto(
                        3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        RaceDto dto = new RaceDto(
                null,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                driver,
                3L,
                team
        );

        // Act
        raceMapper.updateEntity(entity, dto);

        // Assert
        assertEquals(1L, entity.getId()); // ID should not change
        assertEquals(5L, entity.getWinnerId());
        assertEquals(3L, entity.getWinningTeamId());
    }
}
