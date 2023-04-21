package com.avvsion.service.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int order_id;

    @Min(value = 1, message = "Customer id is needed")
    private int customer_id;

    @CreatedDate
    private LocalDate booked_date;

    private Person person;

    private String Status;

    private String comments;

    private LocalDate confirm_date;

    @Min(value = 1, message = "service id is needed")
    private int service_id;
}
