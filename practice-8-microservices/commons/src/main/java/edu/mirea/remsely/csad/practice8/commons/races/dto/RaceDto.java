package edu.mirea.remsely.csad.practice8.commons.races.dto;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceDto {

    private Long id;

    @NotBlank(message = "Race name is required")
    private String name;

    @NotBlank(message = "Circuit is required")
    private String circuit;

    @NotBlank(message = "Country is required")
    private String country;

    @NotNull(message = "Race date is required")
    private LocalDate raceDate;

    private Long winnerId;

    private DriverDto winner;

    private Long winningTeamId;

    private TeamDto winningTeam;
}
