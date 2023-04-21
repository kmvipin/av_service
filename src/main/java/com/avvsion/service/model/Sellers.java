package com.avvsion.service.model;

import javax.persistence.*;
import lombok.Data;

@Data
public class Sellers{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    public int seller_id;

    public Person person;
}
