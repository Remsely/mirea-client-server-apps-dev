package edu.mirea.remsely.csad.practice8.teams.repository;

import edu.mirea.remsely.csad.practice8.teams.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByName(String name);
}
