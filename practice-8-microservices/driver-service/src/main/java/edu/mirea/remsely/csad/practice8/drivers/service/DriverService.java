package edu.mirea.remsely.csad.practice8.drivers.service;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.drivers.exception.DriverNotFoundException;
import edu.mirea.remsely.csad.practice8.drivers.entity.DriverEntity;
import edu.mirea.remsely.csad.practice8.drivers.mapper.DriverMapper;
import edu.mirea.remsely.csad.practice8.drivers.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Transactional(readOnly = true)
    public List<DriverDto> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverDto getDriverById(Long id) {
        return driverRepository.findById(id)
                .map(driverMapper::toDto)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<DriverDto> getDriversByTeamId(Long teamId) {
        return driverRepository.findByTeamId(teamId).stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DriverDto createDriver(DriverDto driverDto) {
        DriverEntity entity = driverMapper.toEntity(driverDto);
        entity.setId(null);
        DriverEntity saved = driverRepository.save(entity);
        return driverMapper.toDto(saved);
    }

    @Transactional
    public DriverDto updateDriver(Long id, DriverDto driverDto) {
        DriverEntity entity = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        driverMapper.updateEntity(entity, driverDto);
        DriverEntity updated = driverRepository.save(entity);
        return driverMapper.toDto(updated);
    }

    @Transactional
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException(id);
        }
        driverRepository.deleteById(id);
    }
}

