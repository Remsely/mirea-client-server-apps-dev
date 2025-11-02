package edu.mirea.remsely.csad.practice8.teams.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.teams.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    void fullTeamCrudFlow_ShouldWorkCorrectly() throws Exception {
        // 1. Создаем новую команду
        TeamDto newTeam = new TeamDto(
                null,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                6
        );

        String createResponse = mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeam)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Red Bull Racing")))
                .andExpect(jsonPath("$.baseLocation", is("Milton Keynes, UK")))
                .andExpect(jsonPath("$.championships", is(6)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TeamDto createdTeam = objectMapper.readValue(createResponse, TeamDto.class);
        Long teamId = createdTeam.getId();

        // 2. Получаем команду по ID
        mockMvc.perform(get("/api/teams/{id}", teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teamId.intValue())))
                .andExpect(jsonPath("$.name", is("Red Bull Racing")));

        // 3. Получаем все команды
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(teamId.intValue())));

        // 4. Обновляем команду (добавляем чемпионство)
        TeamDto updateDto = new TeamDto(
                teamId,
                "Red Bull Racing",
                "Milton Keynes, UK",
                "Christian Horner",
                "Red Bull Powertrains",
                7
        );
        mockMvc.perform(put("/api/teams/{id}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.championships", is(7)));

        // 5. Проверяем обновление
        mockMvc.perform(get("/api/teams/{id}", teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.championships", is(7)));

        // 6. Удаляем команду
        mockMvc.perform(delete("/api/teams/{id}", teamId))
                .andExpect(status().isNoContent());

        // 7. Проверяем, что команда удалена
        mockMvc.perform(get("/api/teams/{id}", teamId))
                .andExpect(status().isNotFound());

        // 8. Проверяем, что список пуст
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getTeamByName_ShouldReturnCorrectTeam() throws Exception {
        // Arrange - создаем несколько команд
        TeamDto team1 = new TeamDto(null, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);
        TeamDto team2 = new TeamDto(null, "Mercedes", "Brackley, UK", "Toto Wolff", "Mercedes", 8);

        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team1)));

        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team2)));

        // Act & Assert - получаем команду по имени
        mockMvc.perform(get("/api/teams/by-name/{name}", "Ferrari"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ferrari")))
                .andExpect(jsonPath("$.baseLocation", is("Maranello, Italy")))
                .andExpect(jsonPath("$.championships", is(16)));
    }

    @Test
    void createTeam_WithInvalidData_ShouldReturnValidationError() throws Exception {
        // Arrange - создаем невалидную команду (пустые обязательные поля)
        TeamDto invalidTeam = new TeamDto(null, "", "", "", null, null);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.baseLocation").exists())
                .andExpect(jsonPath("$.errors.teamPrincipal").exists());
    }

    @Test
    void updateNonExistentTeam_ShouldReturnNotFound() throws Exception {
        // Arrange
        TeamDto updateDto = new TeamDto(999L, "Test Team", "Test Location", "Test Principal", "Test", 0);

        // Act & Assert
        mockMvc.perform(put("/api/teams/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    @Test
    void deleteNonExistentTeam_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void createMultipleTeams_ShouldReturnAllTeams() throws Exception {
        // Arrange & Act - создаем 3 команды
        TeamDto team1 = new TeamDto(null, "Red Bull Racing", "Milton Keynes, UK", "Christian Horner", "Red Bull Powertrains", 6);
        TeamDto team2 = new TeamDto(null, "Ferrari", "Maranello, Italy", "Frédéric Vasseur", "Ferrari", 16);
        TeamDto team3 = new TeamDto(null, "Mercedes", "Brackley, UK", "Toto Wolff", "Mercedes", 8);

        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team1)));

        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team2)));

        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team3)));

        // Assert - проверяем, что все команды созданы
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Red Bull Racing", "Ferrari", "Mercedes")));
    }
}

