package edu.mirea.remsely.csad.practice8.commons.teams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private Long id;

    @NotBlank(message = "Team name is required")
    private String name;

    @NotBlank(message = "Base location is required")
    private String baseLocation;

    @NotBlank(message = "Team principal is required")
    private String teamPrincipal;

    private String engineSupplier;

    private Integer championships;
}

