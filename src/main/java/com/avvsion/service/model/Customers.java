package com.avvsion.service.model;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    private int customer_id;

    @OneToOne
    private Person person;
}
