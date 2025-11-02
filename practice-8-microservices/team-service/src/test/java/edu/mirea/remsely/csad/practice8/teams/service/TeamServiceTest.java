package edu.mirea.remsely.csad.practice8.teams.service;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.commons.teams.exception.TeamNotFoundException;
import edu.mirea.remsely.csad.practice8.teams.entity.TeamEntity;
import edu.mirea.remsely.csad.practice8.teams.mapper.TeamMapper;
import edu.mirea.remsely.csad.practice8.teams.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamService teamService;

    private TeamEntity teamEntity;
    private TeamDto teamDto;

    @BeforeEach
    void setUp() {
        teamEntity = new TeamEntity(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );
        teamDto = new TeamDto(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );
    }

    @Test
    void getAllTeams_ShouldReturnListOfTeams() {
        // Arrange
        List<TeamEntity> entities = Collections.singletonList(teamEntity);
        when(teamRepository.findAll()).thenReturn(entities);
        when(teamMapper.toDto(any(TeamEntity.class))).thenReturn(teamDto);

        // Act
        List<TeamDto> result = teamService.getAllTeams();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    void getTeamById_WhenTeamExists_ShouldReturnTeam() {
        // Arrange
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(teamEntity));
        when(teamMapper.toDto(any(TeamEntity.class))).thenReturn(teamDto);

        // Act
        TeamDto result = teamService.getTeamById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Red Bull Racing", result.getName());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    void getTeamById_WhenTeamNotExists_ShouldThrowException() {
        // Arrange
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(999L));
    }

    @Test
    void getTeamByName_WhenTeamExists_ShouldReturnTeam() {
        // Arrange
        when(teamRepository.findByName(anyString())).thenReturn(Optional.of(teamEntity));
        when(teamMapper.toDto(any(TeamEntity.class))).thenReturn(teamDto);

        // Act
        TeamDto result = teamService.getTeamByName("Red Bull Racing");

        // Assert
        assertNotNull(result);
        assertEquals("Red Bull Racing", result.getName());
        verify(teamRepository, times(1)).findByName("Red Bull Racing");
    }

    @Test
    void getTeamByName_WhenTeamNotExists_ShouldThrowException() {
        // Arrange
        when(teamRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamByName("Unknown Team"));
    }

    @Test
    void createTeam_ShouldReturnCreatedTeam() {
        // Arrange
        when(teamMapper.toEntity(any(TeamDto.class))).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(teamMapper.toDto(any(TeamEntity.class))).thenReturn(teamDto);

        // Act
        TeamDto result = teamService.createTeam(teamDto);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    void updateTeam_WhenTeamExists_ShouldReturnUpdatedTeam() {
        // Arrange
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(teamEntity));
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(teamMapper.toDto(any(TeamEntity.class))).thenReturn(teamDto);

        // Act
        TeamDto result = teamService.updateTeam(1L, teamDto);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    void updateTeam_WhenTeamNotExists_ShouldThrowException() {
        // Arrange
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(999L, teamDto));
    }

    @Test
    void deleteTeam_WhenTeamExists_ShouldDeleteTeam() {
        // Arrange
        when(teamRepository.existsById(anyLong())).thenReturn(true);

        // Act
        teamService.deleteTeam(1L);

        // Assert
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTeam_WhenTeamNotExists_ShouldThrowException() {
        // Arrange
        when(teamRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(999L));
    }
}
