package edu.mirea.remsely.csad.practice8.drivers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.drivers.repository.DriverRepository;
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
class DriverControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
    }

    @Test
    void fullDriverCrudFlow_ShouldWorkCorrectly() throws Exception {
        // 1. Создаем нового пилота
        DriverDto newDriver = new DriverDto(null, "Max", "Verstappen", 1, "Dutch", 1L);

        String createResponse = mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriver)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.lastName", is("Verstappen")))
                .andExpect(jsonPath("$.number", is(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        DriverDto createdDriver = objectMapper.readValue(createResponse, DriverDto.class);
        Long driverId = createdDriver.getId();

        // 2. Получаем пилота по ID
        mockMvc.perform(get("/api/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(driverId.intValue())))
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.lastName", is("Verstappen")));

        // 3. Получаем всех пилотов
        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(driverId.intValue())));

        // 4. Обновляем пилота
        DriverDto updateDto = new DriverDto(driverId, "Max", "Verstappen", 33, "Dutch", 1L);
        mockMvc.perform(put("/api/drivers/{id}", driverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(33)));

        // 5. Проверяем обновление
        mockMvc.perform(get("/api/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(33)));

        // 6. Удаляем пилота
        mockMvc.perform(delete("/api/drivers/{id}", driverId))
                .andExpect(status().isNoContent());

        // 7. Проверяем, что пилот удален
        mockMvc.perform(get("/api/drivers/{id}", driverId))
                .andExpect(status().isNotFound());

        // 8. Проверяем, что список пуст
        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getDriversByTeamId_ShouldReturnOnlyDriversFromThatTeam() throws Exception {
        // Arrange - создаем пилотов разных команд
        DriverDto driver1 = new DriverDto(null, "Max", "Verstappen", 1, "Dutch", 1L);
        DriverDto driver2 = new DriverDto(null, "Sergio", "Perez", 11, "Mexican", 1L);
        DriverDto driver3 = new DriverDto(null, "Lewis", "Hamilton", 44, "British", 2L);

        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driver1)));

        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driver2)));

        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driver3)));

        // Act & Assert - получаем пилотов команды 1
        mockMvc.perform(get("/api/drivers/by-team/{teamId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].teamId", everyItem(is(1))));

        // Act & Assert - получаем пилотов команды 2
        mockMvc.perform(get("/api/drivers/by-team/{teamId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Lewis")));
    }

    @Test
    void createDriver_WithInvalidData_ShouldReturnValidationError() throws Exception {
        // Arrange - создаем невалидного пилота (пустые обязательные поля)
        DriverDto invalidDriver = new DriverDto(null, "", "", null, "", null);

        // Act & Assert
        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDriver)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.lastName").exists())
                .andExpect(jsonPath("$.errors.number").exists())
                .andExpect(jsonPath("$.errors.nationality").exists());
    }

    @Test
    void updateNonExistentDriver_ShouldReturnNotFound() throws Exception {
        // Arrange
        DriverDto updateDto = new DriverDto(999L, "Test", "Driver", 99, "Test", 1L);

        // Act & Assert
        mockMvc.perform(put("/api/drivers/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    @Test
    void deleteNonExistentDriver_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/drivers/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }
}

