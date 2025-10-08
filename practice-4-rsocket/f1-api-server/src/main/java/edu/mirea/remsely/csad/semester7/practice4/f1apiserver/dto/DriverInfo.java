package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverInfo {
    private String name;
    private String team;
    private int number;
}
