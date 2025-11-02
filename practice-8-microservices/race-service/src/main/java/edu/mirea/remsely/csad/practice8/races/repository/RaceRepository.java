package edu.mirea.remsely.csad.practice8.races.repository;

import edu.mirea.remsely.csad.practice8.races.entity.RaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, Long> {
    List<RaceEntity> findByCountry(String country);
    List<RaceEntity> findByWinnerId(Long winnerId);
    List<RaceEntity> findByWinningTeamId(Long teamId);
    List<RaceEntity> findByRaceDateAfter(LocalDate date);
}
