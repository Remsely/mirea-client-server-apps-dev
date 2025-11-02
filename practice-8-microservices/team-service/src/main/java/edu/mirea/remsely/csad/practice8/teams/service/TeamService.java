package edu.mirea.remsely.csad.practice8.teams.service;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.commons.teams.exception.TeamNotFoundException;
import edu.mirea.remsely.csad.practice8.teams.entity.TeamEntity;
import edu.mirea.remsely.csad.practice8.teams.mapper.TeamMapper;
import edu.mirea.remsely.csad.practice8.teams.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Transactional(readOnly = true)
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDto getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toDto)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public TeamDto getTeamByName(String name) {
        return teamRepository.findByName(name)
                .map(teamMapper::toDto)
                .orElseThrow(() -> new TeamNotFoundException(null));
    }

    @Transactional
    public TeamDto createTeam(TeamDto teamDto) {
        TeamEntity entity = teamMapper.toEntity(teamDto);
        entity.setId(null);
        TeamEntity saved = teamRepository.save(entity);
        return teamMapper.toDto(saved);
    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamDto teamDto) {
        TeamEntity entity = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        teamMapper.updateEntity(entity, teamDto);
        TeamEntity updated = teamRepository.save(entity);
        return teamMapper.toDto(updated);
    }

    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new TeamNotFoundException(id);
        }
        teamRepository.deleteById(id);
    }
}

