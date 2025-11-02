package edu.mirea.remsely.csad.practice8.races.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.commons.races.exception.RaceNotFoundException;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.races.service.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RaceController.class)
class RaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RaceService raceService;

    private RaceDto raceDto;

    @BeforeEach
    void setUp() {
        DriverDto driver = new DriverDto(5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        TeamDto team = new TeamDto(3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        raceDto = new RaceDto(
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
    }

    @Test
    void getAllRaces_ShouldReturnListOfRaces() throws Exception {
        // Arrange
        when(raceService.getAllRaces()).thenReturn(Collections.singletonList(raceDto));

        // Act & Assert
        mockMvc.perform(get("/api/races"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Monaco Grand Prix")))
                .andExpect(jsonPath("$[0].winner.firstName", is("Charles")))
                .andExpect(jsonPath("$[0].winner.lastName", is("Leclerc")))
                .andExpect(jsonPath("$[0].winningTeam.name", is("Ferrari")));

        verify(raceService, times(1)).getAllRaces();
    }

    @Test
    void getRaceById_WhenRaceExists_ShouldReturnRace() throws Exception {
        // Arrange
        when(raceService.getRaceById(1L)).thenReturn(raceDto);

        // Act & Assert
        mockMvc.perform(get("/api/races/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Monaco Grand Prix")))
                .andExpect(jsonPath("$.circuit", is("Circuit de Monaco")))
                .andExpect(jsonPath("$.country", is("Monaco")))
                .andExpect(jsonPath("$.winner.firstName", is("Charles")))
                .andExpect(jsonPath("$.winner.lastName", is("Leclerc")))
                .andExpect(jsonPath("$.winningTeam.name", is("Ferrari")));

        verify(raceService, times(1)).getRaceById(1L);
    }

    @Test
    void getRaceById_WhenRaceNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(raceService.getRaceById(999L)).thenThrow(new RaceNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/api/races/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(raceService, times(1)).getRaceById(999L);
    }

    @Test
    void getRacesByCountry_ShouldReturnFilteredRaces() throws Exception {
        // Arrange
        when(raceService.getRacesByCountry("Monaco")).thenReturn(Collections.singletonList(raceDto));

        // Act & Assert
        mockMvc.perform(get("/api/races/by-country/{country}", "Monaco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].country", is("Monaco")));

        verify(raceService, times(1)).getRacesByCountry("Monaco");
    }

    @Test
    void createRace_WithValidData_ShouldReturnCreatedRace() throws Exception {
        // Arrange
        RaceDto newRaceDto = new RaceDto(
                null,
                "Bahrain Grand Prix",
                "Bahrain International Circuit",
                "Bahrain",
                LocalDate.of(2025, 3, 1),
                null,
                null,
                null,
                null
        );
        RaceDto savedRaceDto = new RaceDto(
                2L,
                "Bahrain Grand Prix",
                "Bahrain International Circuit",
                "Bahrain",
                LocalDate.of(2025, 3, 1),
                null,
                null,
                null,
                null
        );
        when(raceService.createRace(any(RaceDto.class))).thenReturn(savedRaceDto);

        // Act & Assert
        mockMvc.perform(post("/api/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRaceDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Bahrain Grand Prix")));

        verify(raceService, times(1)).createRace(any(RaceDto.class));
    }

    @Test
    void createRace_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - пустые обязательные поля
        RaceDto invalidRaceDto = new RaceDto(null, "", "", "", null, null, null, null, null);

        // Act & Assert
        mockMvc.perform(post("/api/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRaceDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")));

        verify(raceService, never()).createRace(any(RaceDto.class));
    }

    @Test
    void updateRace_WithValidData_ShouldReturnUpdatedRace() throws Exception {
        // Arrange
        DriverDto driver = new DriverDto(5L, "Charles", "Leclerc", 16, "Monegasque", 3L);
        TeamDto team = new TeamDto(3L, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);

        RaceDto updatedRaceDto = new RaceDto(
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
        when(raceService.updateRace(eq(1L), any(RaceDto.class))).thenReturn(updatedRaceDto);

        // Act & Assert
        mockMvc.perform(put("/api/races/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRaceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(raceService, times(1)).updateRace(eq(1L), any(RaceDto.class));
    }

    @Test
    void deleteRace_WhenRaceExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(raceService).deleteRace(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/races/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(raceService, times(1)).deleteRace(1L);
    }

    @Test
    void getUpcomingRaces_ShouldReturnFutureRaces() throws Exception {
        // Arrange
        RaceDto futureRace = new RaceDto(
                2L,
                "Bahrain Grand Prix 2025",
                "Bahrain International Circuit",
                "Bahrain",
                LocalDate.of(2025, 3, 1),
                null,
                null,
                null,
                null
        );
        when(raceService.getUpcomingRaces()).thenReturn(List.of(futureRace));

        // Act & Assert
        mockMvc.perform(get("/api/races/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Bahrain Grand Prix 2025")));

        verify(raceService, times(1)).getUpcomingRaces();
    }
}
