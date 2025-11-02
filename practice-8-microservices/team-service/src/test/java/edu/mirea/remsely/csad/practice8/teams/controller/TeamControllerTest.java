package edu.mirea.remsely.csad.practice8.teams.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.commons.teams.exception.TeamNotFoundException;
import edu.mirea.remsely.csad.practice8.teams.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeamService teamService;

    private TeamDto teamDto;
    private List<TeamDto> teamsList;

    @BeforeEach
    void setUp() {
        teamDto = new TeamDto(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );
        TeamDto teamDto2 = new TeamDto(
                2L,
                "Ferrari",
                "Maranello, Italy",
                "Frédéric Vasseur",
                "Ferrari",
                16
        );
        teamsList = Arrays.asList(teamDto, teamDto2);
    }

    @Test
    void getAllTeams_ShouldReturnListOfTeams() throws Exception {
        // Arrange
        when(teamService.getAllTeams()).thenReturn(teamsList);

        // Act & Assert
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Red Bull Racing")))
                .andExpect(jsonPath("$[0].baseLocation", is("Milton Keynes, UK")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Ferrari")));

        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    void getTeamById_WhenTeamExists_ShouldReturnTeam() throws Exception {
        // Arrange
        when(teamService.getTeamById(1L)).thenReturn(teamDto);

        // Act & Assert
        mockMvc.perform(get("/api/teams/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Red Bull Racing")))
                .andExpect(jsonPath("$.baseLocation", is("Milton Keynes, UK")))
                .andExpect(jsonPath("$.teamPrincipal", is("Christian Horner")))
                .andExpect(jsonPath("$.engineSupplier", is("Red Bull Powertrains")))
                .andExpect(jsonPath("$.championships", is(6)));

        verify(teamService, times(1)).getTeamById(1L);
    }

    @Test
    void getTeamById_WhenTeamNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(teamService.getTeamById(999L)).thenThrow(new TeamNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/api/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message").exists());

        verify(teamService, times(1)).getTeamById(999L);
    }

    @Test
    void getTeamByName_ShouldReturnTeam() throws Exception {
        // Arrange
        when(teamService.getTeamByName("Red Bull Racing")).thenReturn(teamDto);

        // Act & Assert
        mockMvc.perform(get("/api/teams/by-name/{name}", "Red Bull Racing"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Red Bull Racing")));

        verify(teamService, times(1)).getTeamByName("Red Bull Racing");
    }

    @Test
    void createTeam_WithValidData_ShouldReturnCreatedTeam() throws Exception {
        // Arrange
        TeamDto newTeamDto = new TeamDto(
                null,
                "McLaren",
                "Woking, UK",
                "Andrea Stella",
                "Mercedes",
                8
        );
        TeamDto savedTeamDto = new TeamDto(
                3L,
                "McLaren",
                "Woking, UK",
                "Andrea Stella",
                "Mercedes",
                8
        );
        when(teamService.createTeam(any(TeamDto.class))).thenReturn(savedTeamDto);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeamDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("McLaren")))
                .andExpect(jsonPath("$.baseLocation", is("Woking, UK")))
                .andExpect(jsonPath("$.teamPrincipal", is("Andrea Stella")))
                .andExpect(jsonPath("$.engineSupplier", is("Mercedes")))
                .andExpect(jsonPath("$.championships", is(8)));

        verify(teamService, times(1)).createTeam(any(TeamDto.class));
    }

    @Test
    void createTeam_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - создаем DTO без обязательных полей
        TeamDto invalidTeamDto = new TeamDto(null, "", "", "", null, null);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeamDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").exists());

        verify(teamService, never()).createTeam(any(TeamDto.class));
    }

    @Test
    void updateTeam_WithValidData_ShouldReturnUpdatedTeam() throws Exception {
        // Arrange
        TeamDto updatedTeamDto = new TeamDto(
                1L,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                7
        );
        when(teamService.updateTeam(eq(1L), any(TeamDto.class))).thenReturn(updatedTeamDto);

        // Act & Assert
        mockMvc.perform(put("/api/teams/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTeamDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.championships", is(7)));

        verify(teamService, times(1)).updateTeam(eq(1L), any(TeamDto.class));
    }

    @Test
    void updateTeam_WhenTeamNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        TeamDto updateDto = new TeamDto(
                999L,
                "Test Team",
                "Test Location",
                "Test Principal",
                "Test Engine",
                0
        );
        when(teamService.updateTeam(eq(999L), any(TeamDto.class)))
                .thenThrow(new TeamNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(put("/api/teams/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)));

        verify(teamService, times(1)).updateTeam(eq(999L), any(TeamDto.class));
    }

    @Test
    void deleteTeam_WhenTeamExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(teamService).deleteTeam(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/teams/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).deleteTeam(1L);
    }

    @Test
    void deleteTeam_WhenTeamNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new TeamNotFoundException(999L)).when(teamService).deleteTeam(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)));

        verify(teamService, times(1)).deleteTeam(999L);
    }
}

