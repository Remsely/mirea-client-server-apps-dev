package edu.mirea.remsely.csad.practice8.drivers.service;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.drivers.exception.DriverNotFoundException;
import edu.mirea.remsely.csad.practice8.drivers.entity.DriverEntity;
import edu.mirea.remsely.csad.practice8.drivers.mapper.DriverMapper;
import edu.mirea.remsely.csad.practice8.drivers.repository.DriverRepository;
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
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverService driverService;

    private DriverEntity driverEntity;
    private DriverDto driverDto;

    @BeforeEach
    void setUp() {
        driverEntity = new DriverEntity(1L, "Max", "Verstappen", 1, "Dutch", 1L);
        driverDto = new DriverDto(1L, "Max", "Verstappen", 1, "Dutch", 1L);
    }

    @Test
    void getAllDrivers_ShouldReturnListOfDrivers() {
        // Arrange
        List<DriverEntity> entities = Collections.singletonList(driverEntity);
        when(driverRepository.findAll()).thenReturn(entities);
        when(driverMapper.toDto(any(DriverEntity.class))).thenReturn(driverDto);

        // Act
        List<DriverDto> result = driverService.getAllDrivers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void getDriverById_WhenDriverExists_ShouldReturnDriver() {
        // Arrange
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driverEntity));
        when(driverMapper.toDto(any(DriverEntity.class))).thenReturn(driverDto);

        // Act
        DriverDto result = driverService.getDriverById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Max", result.getFirstName());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_WhenDriverNotExists_ShouldThrowException() {
        // Arrange
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverById(999L));
    }

    @Test
    void createDriver_ShouldReturnCreatedDriver() {
        // Arrange
        when(driverMapper.toEntity(any(DriverDto.class))).thenReturn(driverEntity);
        when(driverRepository.save(any(DriverEntity.class))).thenReturn(driverEntity);
        when(driverMapper.toDto(any(DriverEntity.class))).thenReturn(driverDto);

        // Act
        DriverDto result = driverService.createDriver(driverDto);

        // Assert
        assertNotNull(result);
        verify(driverRepository, times(1)).save(any(DriverEntity.class));
    }

    @Test
    void updateDriver_WhenDriverExists_ShouldReturnUpdatedDriver() {
        // Arrange
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driverEntity));
        when(driverRepository.save(any(DriverEntity.class))).thenReturn(driverEntity);
        when(driverMapper.toDto(any(DriverEntity.class))).thenReturn(driverDto);

        // Act
        DriverDto result = driverService.updateDriver(1L, driverDto);

        // Assert
        assertNotNull(result);
        verify(driverRepository, times(1)).save(any(DriverEntity.class));
    }

    @Test
    void updateDriver_WhenDriverNotExists_ShouldThrowException() {
        // Arrange
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver(999L, driverDto));
    }

    @Test
    void deleteDriver_WhenDriverExists_ShouldDeleteDriver() {
        // Arrange
        when(driverRepository.existsById(anyLong())).thenReturn(true);

        // Act
        driverService.deleteDriver(1L);

        // Assert
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDriver_WhenDriverNotExists_ShouldThrowException() {
        // Arrange
        when(driverRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(999L));
    }

    @Test
    void getDriversByTeamId_ShouldReturnListOfDrivers() {
        // Arrange
        List<DriverEntity> entities = Collections.singletonList(driverEntity);
        when(driverRepository.findByTeamId(anyLong())).thenReturn(entities);
        when(driverMapper.toDto(any(DriverEntity.class))).thenReturn(driverDto);

        // Act
        List<DriverDto> result = driverService.getDriversByTeamId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(driverRepository, times(1)).findByTeamId(1L);
    }
}
