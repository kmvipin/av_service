package com.avvsion.service.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int payment_id;

    @NotBlank(message = "payment_method is needed")
    private String payment_method;

    private Orders order;

    @CreatedDate
    private LocalDate date;

    private BigDecimal amount;
}
