package edu.mirea.remsely.csad.practice8.drivers.repository;

import edu.mirea.remsely.csad.practice8.drivers.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    List<DriverEntity> findByTeamId(Long teamId);
}
