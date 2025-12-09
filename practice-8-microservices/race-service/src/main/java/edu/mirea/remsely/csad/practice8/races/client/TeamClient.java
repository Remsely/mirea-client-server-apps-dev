package edu.mirea.remsely.csad.practice8.races.client;

import edu.mirea.remsely.csad.practice8.commons.teams.dto.TeamDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service")
public interface TeamClient {
    @GetMapping("/api/teams/{id}")
    TeamDto getTeamById(@PathVariable Long id);
}
