package com.avvsion.service.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int address_id;

    public Address(){

    }
    public Address( String address1, String address2, String city, String state, String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @Size(min = 5, message = "Address1 must be at least 5 character long")
    private String address1;

    private String address2;

    @Size(min = 5,message = "City must be at least 5 character long")
    private String city;

    @Size(min = 3, message = "State must be at least 3 character long")
    private String state;

    @Pattern(regexp = "(^$|[0-9]{5})",message = "Zip code must be 5 digits")
    private String zipCode;
}
