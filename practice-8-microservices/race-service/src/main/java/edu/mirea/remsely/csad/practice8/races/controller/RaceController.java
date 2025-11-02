package edu.mirea.remsely.csad.practice8.races.controller;

import edu.mirea.remsely.csad.practice8.commons.races.dto.RaceDto;
import edu.mirea.remsely.csad.practice8.races.service.RaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/races")
@RequiredArgsConstructor
public class RaceController {

    private final RaceService raceService;

    @GetMapping
    public ResponseEntity<List<RaceDto>> getAllRaces() {
        return ResponseEntity.ok(raceService.getAllRaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceDto> getRaceById(@PathVariable Long id) {
        return ResponseEntity.ok(raceService.getRaceById(id));
    }

    @GetMapping("/by-country/{country}")
    public ResponseEntity<List<RaceDto>> getRacesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(raceService.getRacesByCountry(country));
    }

    @GetMapping("/by-winner/{winnerId}")
    public ResponseEntity<List<RaceDto>> getRacesByWinner(@PathVariable Long winnerId) {
        return ResponseEntity.ok(raceService.getRacesByWinner(winnerId));
    }

    @GetMapping("/by-team/{teamId}")
    public ResponseEntity<List<RaceDto>> getRacesByWinningTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(raceService.getRacesByWinningTeam(teamId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<RaceDto>> getUpcomingRaces() {
        return ResponseEntity.ok(raceService.getUpcomingRaces());
    }

    @PostMapping
    public ResponseEntity<RaceDto> createRace(@Valid @RequestBody RaceDto raceDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(raceService.createRace(raceDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceDto> updateRace(@PathVariable Long id,
                                              @Valid @RequestBody RaceDto raceDto) {
        return ResponseEntity.ok(raceService.updateRace(id, raceDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }
}
