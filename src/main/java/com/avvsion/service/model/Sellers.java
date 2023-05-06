package com.avvsion.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Sellers{

    @JsonIgnore
    public int seller_id;

    public Person person;
}
