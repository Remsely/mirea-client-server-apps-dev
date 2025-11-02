package edu.mirea.remsely.csad.practice8.drivers.mapper;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.drivers.entity.DriverEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriverMapperTest {

    private DriverMapper driverMapper;

    @BeforeEach
    void setUp() {
        driverMapper = new DriverMapper();
    }

    @Test
    void toDto_ShouldConvertEntityToDto() {
        // Arrange
        DriverEntity entity = new DriverEntity(1L, "Lewis", "Hamilton", 44, "British", 2L);

        // Act
        DriverDto dto = driverMapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getNumber(), dto.getNumber());
        assertEquals(entity.getNationality(), dto.getNationality());
        assertEquals(entity.getTeamId(), dto.getTeamId());
    }

    @Test
    void toEntity_ShouldConvertDtoToEntity() {
        // Arrange
        DriverDto dto = new DriverDto(1L, "Lewis", "Hamilton", 44, "British", 2L);

        // Act
        DriverEntity entity = driverMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getNumber(), entity.getNumber());
        assertEquals(dto.getNationality(), entity.getNationality());
        assertEquals(dto.getTeamId(), entity.getTeamId());
    }

    @Test
    void updateEntity_ShouldUpdateExistingEntity() {
        // Arrange
        DriverEntity entity = new DriverEntity(1L, "Lewis", "Hamilton", 44, "British", 2L);
        DriverDto dto = new DriverDto(null, "Fernando", "Alonso", 14, "Spanish", 3L);

        // Act
        driverMapper.updateEntity(entity, dto);

        // Assert
        assertEquals(1L, entity.getId()); // ID should not change
        assertEquals("Fernando", entity.getFirstName());
        assertEquals("Alonso", entity.getLastName());
        assertEquals(14, entity.getNumber());
        assertEquals("Spanish", entity.getNationality());
        assertEquals(3L, entity.getTeamId());
    }
}

