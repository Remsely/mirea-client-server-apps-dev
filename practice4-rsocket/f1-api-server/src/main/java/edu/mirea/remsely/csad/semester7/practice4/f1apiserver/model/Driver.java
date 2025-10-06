package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("drivers")
public class Driver {
    @Id
    private Long id;

    private String name;

    private String team;

    private Integer number;
}
