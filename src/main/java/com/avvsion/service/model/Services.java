package com.avvsion.service.model;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Services {

    private int service_id;

    @JsonIgnore
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
