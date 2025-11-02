package edu.mirea.remsely.csad.practice8.races.service;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.commons.races.exception.RaceNotFoundException;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.races.client.DriverClient;
import edu.mirea.remsely.csad.practice8.races.client.TeamClient;
import edu.mirea.remsely.csad.practice8.races.entity.RaceEntity;
import edu.mirea.remsely.csad.practice8.races.mapper.RaceMapper;
import edu.mirea.remsely.csad.practice8.races.repository.RaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private RaceMapper raceMapper;

    @Mock
    private DriverClient driverClient;

    @Mock
    private TeamClient teamClient;

    @InjectMocks
    private RaceService raceService;

    private RaceEntity raceEntity;
    private RaceDto raceDto;
    private DriverDto driverDto;
    private TeamDto teamDto;

    @BeforeEach
    void setUp() {
        raceEntity = new RaceEntity(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                3L
        );

        driverDto = new DriverDto(5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        teamDto = new TeamDto(3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        raceDto = new RaceDto(
                1L,
                "Monaco Grand Prix",
                "Circuit de Monaco",
                "Monaco",
                LocalDate.of(2024, 5, 26),
                5L,
                driverDto,
                3L,
                teamDto
        );
    }

    @Test
    void getAllRaces_ShouldReturnEnrichedListOfRaces() {
        // Arrange
        when(raceRepository.findAll()).thenReturn(Collections.singletonList(raceEntity));
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        List<RaceDto> result = raceService.getAllRaces();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(raceRepository, times(1)).findAll();
        verify(driverClient, times(1)).getDriverById(5L);
        verify(teamClient, times(1)).getTeamById(3L);
    }

    @Test
    void getRaceById_WhenRaceExists_ShouldReturnEnrichedRace() {
        // Arrange
        when(raceRepository.findById(anyLong())).thenReturn(Optional.of(raceEntity));
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        RaceDto result = raceService.getRaceById(1L);

        // Assert
        assertNotNull(result);
        verify(raceRepository, times(1)).findById(1L);
    }

    @Test
    void getRaceById_WhenRaceNotExists_ShouldThrowException() {
        // Arrange
        when(raceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RaceNotFoundException.class, () -> raceService.getRaceById(999L));
    }

    @Test
    void getRacesByCountry_ShouldReturnFilteredRaces() {
        // Arrange
        when(raceRepository.findByCountry(anyString())).thenReturn(Collections.singletonList(raceEntity));
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        List<RaceDto> result = raceService.getRacesByCountry("Monaco");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(raceRepository, times(1)).findByCountry("Monaco");
    }

    @Test
    void getRacesByWinner_ShouldReturnFilteredRaces() {
        // Arrange
        when(raceRepository.findByWinnerId(anyLong())).thenReturn(Collections.singletonList(raceEntity));
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        List<RaceDto> result = raceService.getRacesByWinner(5L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(raceRepository, times(1)).findByWinnerId(5L);
    }

    @Test
    void getRacesByWinningTeam_ShouldReturnFilteredRaces() {
        // Arrange
        when(raceRepository.findByWinningTeamId(anyLong())).thenReturn(Collections.singletonList(raceEntity));
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        List<RaceDto> result = raceService.getRacesByWinningTeam(3L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(raceRepository, times(1)).findByWinningTeamId(3L);
    }

    @Test
    void createRace_ShouldReturnCreatedRace() {
        // Arrange
        when(raceMapper.toEntity(any(RaceDto.class))).thenReturn(raceEntity);
        when(raceRepository.save(any(RaceEntity.class))).thenReturn(raceEntity);
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        RaceDto result = raceService.createRace(raceDto);

        // Assert
        assertNotNull(result);
        verify(raceRepository, times(1)).save(any(RaceEntity.class));
    }

    @Test
    void updateRace_WhenRaceExists_ShouldReturnUpdatedRace() {
        // Arrange
        when(raceRepository.findById(anyLong())).thenReturn(Optional.of(raceEntity));
        when(raceRepository.save(any(RaceEntity.class))).thenReturn(raceEntity);
        when(driverClient.getDriverById(anyLong())).thenReturn(driverDto);
        when(teamClient.getTeamById(anyLong())).thenReturn(teamDto);
        when(raceMapper.toDtoWithDetails(any(), any(), any())).thenReturn(raceDto);

        // Act
        RaceDto result = raceService.updateRace(1L, raceDto);

        // Assert
        assertNotNull(result);
        verify(raceRepository, times(1)).save(any(RaceEntity.class));
    }

    @Test
    void updateRace_WhenRaceNotExists_ShouldThrowException() {
        // Arrange
        when(raceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RaceNotFoundException.class, () -> raceService.updateRace(999L, raceDto));
    }

    @Test
    void deleteRace_WhenRaceExists_ShouldDeleteRace() {
        // Arrange
        when(raceRepository.existsById(anyLong())).thenReturn(true);

        // Act
        raceService.deleteRace(1L);

        // Assert
        verify(raceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRace_WhenRaceNotExists_ShouldThrowException() {
        // Arrange
        when(raceRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(RaceNotFoundException.class, () -> raceService.deleteRace(999L));
    }
}
