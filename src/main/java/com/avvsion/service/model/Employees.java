package com.avvsion.service.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Employees extends Person{

    @JsonIgnore
    private int employee_id;

    @JsonIgnore
    private int person_id;

    @NotBlank(message = "Job Title must not be blank")
    private String job_title;

    @NotBlank(message = "salary must not be blank")
    private BigDecimal salary;

    @NotBlank(message = "hire date must not be blank")
    @DateTimeFormat
    private LocalDate hire_date;
}
