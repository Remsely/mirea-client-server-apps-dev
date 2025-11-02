package edu.mirea.remsely.csad.practice8.commons.drivers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Number is required")
    @Min(value = 1, message = "Number must be positive")
    private Integer number;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    private Long teamId;
}
