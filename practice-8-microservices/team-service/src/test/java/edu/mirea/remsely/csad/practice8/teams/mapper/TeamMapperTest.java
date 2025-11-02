package edu.mirea.remsely.csad.practice8.teams.mapper;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.teams.entity.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamMapperTest {
    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        teamMapper = new TeamMapper();
    }

    @Test
    void toDto_ShouldConvertEntityToDto() {
        // Arrange
        TeamEntity entity = new TeamEntity(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );

        // Act
        TeamDto dto = teamMapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getBaseLocation(), dto.getBaseLocation());
        assertEquals(entity.getTeamPrincipal(), dto.getTeamPrincipal());
        assertEquals(entity.getEngineSupplier(), dto.getEngineSupplier());
        assertEquals(entity.getChampionships(), dto.getChampionships());
    }

    @Test
    void toEntity_ShouldConvertDtoToEntity() {
        // Arrange
        TeamDto dto = new TeamDto(
                1L,
                "Ferrari",
                "Maranello, Italy",
                "Frédéric Vasseur",
                "Ferrari",
                16
        );

        // Act
        TeamEntity entity = teamMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getBaseLocation(), entity.getBaseLocation());
        assertEquals(dto.getTeamPrincipal(), entity.getTeamPrincipal());
        assertEquals(dto.getEngineSupplier(), entity.getEngineSupplier());
        assertEquals(dto.getChampionships(), entity.getChampionships());
    }

    @Test
    void updateEntity_ShouldUpdateExistingEntity() {
        // Arrange
        TeamEntity entity = new TeamEntity(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                5
        );
        TeamDto dto = new TeamDto(
                null,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );

        // Act
        teamMapper.updateEntity(entity, dto);

        // Assert
        assertEquals(1L, entity.getId()); // ID should not change
        assertEquals("Red Bull Racing", entity.getName());
        assertEquals("Milton Keynes, UK", entity.getBaseLocation());
        assertEquals("Christian Horner", entity.getTeamPrincipal());
        assertEquals("Red Bull Powertrains", entity.getEngineSupplier());
        assertEquals(6, entity.getChampionships());
    }
}

