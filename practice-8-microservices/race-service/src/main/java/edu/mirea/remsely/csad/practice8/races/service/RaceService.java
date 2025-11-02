package edu.mirea.remsely.csad.practice8.races.service;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.commons.races.exception.RaceNotFoundException;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.races.client.DriverClient;
import edu.mirea.remsely.csad.practice8.races.client.TeamClient;
import edu.mirea.remsely.csad.practice8.races.entity.RaceEntity;
import edu.mirea.remsely.csad.practice8.races.mapper.RaceMapper;
import edu.mirea.remsely.csad.practice8.races.repository.RaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RaceService {

    private final RaceRepository raceRepository;
    private final RaceMapper raceMapper;
    private final DriverClient driverClient;
    private final TeamClient teamClient;

    @Transactional(readOnly = true)
    public List<RaceDto> getAllRaces() {
        return raceRepository.findAll().stream()
                .map(this::enrichRaceWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RaceDto getRaceById(Long id) {
        RaceEntity entity = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));
        return enrichRaceWithDetails(entity);
    }

    @Transactional(readOnly = true)
    public List<RaceDto> getRacesByCountry(String country) {
        return raceRepository.findByCountry(country).stream()
                .map(this::enrichRaceWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RaceDto> getRacesByWinner(Long winnerId) {
        return raceRepository.findByWinnerId(winnerId).stream()
                .map(this::enrichRaceWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RaceDto> getRacesByWinningTeam(Long teamId) {
        return raceRepository.findByWinningTeamId(teamId).stream()
                .map(this::enrichRaceWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RaceDto> getUpcomingRaces() {
        return raceRepository.findByRaceDateAfter(LocalDate.now()).stream()
                .map(this::enrichRaceWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional
    public RaceDto createRace(RaceDto raceDto) {
        RaceEntity entity = raceMapper.toEntity(raceDto);
        entity.setId(null);
        RaceEntity saved = raceRepository.save(entity);
        return enrichRaceWithDetails(saved);
    }

    @Transactional
    public RaceDto updateRace(Long id, RaceDto raceDto) {
        RaceEntity entity = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));

        raceMapper.updateEntity(entity, raceDto);
        RaceEntity updated = raceRepository.save(entity);
        return enrichRaceWithDetails(updated);
    }

    @Transactional
    public void deleteRace(Long id) {
        if (!raceRepository.existsById(id)) {
            throw new RaceNotFoundException(id);
        }
        raceRepository.deleteById(id);
    }

    private RaceDto enrichRaceWithDetails(RaceEntity entity) {
        DriverDto winner = null;
        TeamDto winningTeam = null;

        // Получаем победителя через Feign Client
        if (entity.getWinnerId() != null) {
            try {
                winner = driverClient.getDriverById(entity.getWinnerId());
            } catch (Exception e) {
                log.warn("Failed to fetch driver with id {}: {}", entity.getWinnerId(), e.getMessage());
            }
        }

        // Получаем команду-победителя через Feign Client
        if (entity.getWinningTeamId() != null) {
            try {
                winningTeam = teamClient.getTeamById(entity.getWinningTeamId());
            } catch (Exception e) {
                log.warn("Failed to fetch team with id {}: {}", entity.getWinningTeamId(), e.getMessage());
            }
        }

        return raceMapper.toDtoWithDetails(entity, winner, winningTeam);
    }
}
