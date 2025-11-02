package edu.mirea.remsely.csad.practice8.drivers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.drivers.exception.DriverNotFoundException;
import edu.mirea.remsely.csad.practice8.drivers.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    private DriverDto driverDto;
    private List<DriverDto> driversList;

    @BeforeEach
    void setUp() {
        driverDto = new DriverDto(1L, "Max", "Verstappen", 1, "Dutch", 1L);
        DriverDto driverDto2 = new DriverDto(2L, "Lewis", "Hamilton", 44, "British", 3L);
        driversList = Arrays.asList(driverDto, driverDto2);
    }

    @Test
    void getAllDrivers_ShouldReturnListOfDrivers() throws Exception {
        // Arrange
        when(driverService.getAllDrivers()).thenReturn(driversList);

        // Act & Assert
        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Max")))
                .andExpect(jsonPath("$[0].lastName", is("Verstappen")))
                .andExpect(jsonPath("$[0].number", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Lewis")))
                .andExpect(jsonPath("$[1].lastName", is("Hamilton")));


        verify(driverService, times(1)).getAllDrivers();
    }

    @Test
    void getDriverById_WhenDriverExists_ShouldReturnDriver() throws Exception {
        // Arrange
        when(driverService.getDriverById(1L)).thenReturn(driverDto);

        // Act & Assert
        mockMvc.perform(get("/api/drivers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Max")))
                .andExpect(jsonPath("$.lastName", is("Verstappen")))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.nationality", is("Dutch")))
                .andExpect(jsonPath("$.teamId", is(1)));

        verify(driverService, times(1)).getDriverById(1L);
    }

    @Test
    void getDriverById_WhenDriverNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(driverService.getDriverById(999L)).thenThrow(new DriverNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/api/drivers/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message").exists());

        verify(driverService, times(1)).getDriverById(999L);
    }

    @Test
    void getDriversByTeamId_ShouldReturnListOfDrivers() throws Exception {
        // Arrange
        List<DriverDto> teamDrivers = Collections.singletonList(driverDto);
        when(driverService.getDriversByTeamId(1L)).thenReturn(teamDrivers);

        // Act & Assert
        mockMvc.perform(get("/api/drivers/by-team/{teamId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Max")))
                .andExpect(jsonPath("$[0].teamId", is(1)));

        verify(driverService, times(1)).getDriversByTeamId(1L);
    }

    @Test
    void createDriver_WithValidData_ShouldReturnCreatedDriver() throws Exception {
        // Arrange
        DriverDto newDriverDto = new DriverDto(null, "Fernando", "Alonso", 14, "Spanish", 5L);
        DriverDto savedDriverDto = new DriverDto(3L, "Fernando", "Alonso", 14, "Spanish", 5L);
        when(driverService.createDriver(any(DriverDto.class))).thenReturn(savedDriverDto);

        // Act & Assert
        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.firstName", is("Fernando")))
                .andExpect(jsonPath("$.lastName", is("Alonso")))
                .andExpect(jsonPath("$.number", is(14)))
                .andExpect(jsonPath("$.nationality", is("Spanish")))
                .andExpect(jsonPath("$.teamId", is(5)));

        verify(driverService, times(1)).createDriver(any(DriverDto.class));
    }

    @Test
    void createDriver_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - создаем DTO без обязательных полей
        DriverDto invalidDriverDto = new DriverDto(null, "", "", null, "", null);

        // Act & Assert
        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDriverDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").exists());

        verify(driverService, never()).createDriver(any(DriverDto.class));
    }

    @Test
    void updateDriver_WithValidData_ShouldReturnUpdatedDriver() throws Exception {
        // Arrange
        DriverDto updatedDriverDto = new DriverDto(1L, "Max", "Verstappen", 33, "Dutch", 1L);
        when(driverService.updateDriver(eq(1L), any(DriverDto.class))).thenReturn(updatedDriverDto);

        // Act & Assert
        mockMvc.perform(put("/api/drivers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDriverDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is(33)));

        verify(driverService, times(1)).updateDriver(eq(1L), any(DriverDto.class));
    }

    @Test
    void updateDriver_WhenDriverNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        DriverDto updateDto = new DriverDto(999L, "Test", "Driver", 99, "Test", 1L);
        when(driverService.updateDriver(eq(999L), any(DriverDto.class)))
                .thenThrow(new DriverNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(put("/api/drivers/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)));

        verify(driverService, times(1)).updateDriver(eq(999L), any(DriverDto.class));
    }

    @Test
    void deleteDriver_WhenDriverExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(driverService).deleteDriver(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/drivers/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(driverService, times(1)).deleteDriver(1L);
    }

    @Test
    void deleteDriver_WhenDriverNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new DriverNotFoundException(999L)).when(driverService).deleteDriver(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/drivers/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)));

        verify(driverService, times(1)).deleteDriver(999L);
    }
}
