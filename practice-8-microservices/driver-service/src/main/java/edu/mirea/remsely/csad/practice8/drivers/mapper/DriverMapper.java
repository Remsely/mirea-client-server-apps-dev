package edu.mirea.remsely.csad.practice8.drivers.mapper;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.drivers.entity.DriverEntity;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverDto toDto(DriverEntity entity) {
        return new DriverDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getNumber(),
                entity.getNationality(),
                entity.getTeamId()
        );
    }

    public DriverEntity toEntity(DriverDto dto) {
        return new DriverEntity(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getNumber(),
                dto.getNationality(),
                dto.getTeamId()
        );
    }

    public void updateEntity(DriverEntity entity, DriverDto dto) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setNumber(dto.getNumber());
        entity.setNationality(dto.getNationality());
        entity.setTeamId(dto.getTeamId());
    }
}

