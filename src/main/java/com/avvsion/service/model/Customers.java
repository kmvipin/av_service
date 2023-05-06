package com.avvsion.service.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class Customers {

    @JsonIgnore
    private int customer_id;

    @OneToOne
    private Person person;
}
