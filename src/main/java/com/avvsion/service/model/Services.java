package com.avvsion.service.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int service_id;

    private int seller_id;

    @NotBlank(message = "Service name must not be blank")
    private String service_name;

    @Min(value = 100, message = "unit price must not be blank")
    private int unit_price;

    private String image;

    @NotBlank(message = "Category must not be blank")
    private String category_name;

    private String message;
}
