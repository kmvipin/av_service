package com.avvsion.service.model;

import com.fasterxml.jackson.databind.ser.Serializers;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Employees extends Person{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int employee_id;

    private int person_id;

    @NotBlank(message = "Job Title must not be blank")
    private String job_title;

    @NotBlank(message = "salary must not be blank")
    private BigDecimal salary;

    @NotBlank(message = "hire date must not be blank")
    @DateTimeFormat
    private LocalDate hire_date;
}
