package edu.mirea.remsely.csad.practice8.races.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "races")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String circuit;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate raceDate;

    private Long winnerId;

    private Long winningTeamId;
}
