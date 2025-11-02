package edu.mirea.remsely.csad.practice8.teams.controller;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import edu.mirea.remsely.csad.practice8.teams.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<TeamDto> getTeamByName(@PathVariable String name) {
        return ResponseEntity.ok(teamService.getTeamByName(name));
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(teamDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable Long id,
                                              @Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
