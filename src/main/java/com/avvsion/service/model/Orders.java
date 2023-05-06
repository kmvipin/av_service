package com.avvsion.service.model;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;
@Data
public class Orders {

    @JsonIgnore
    private int order_id;

    @JsonIgnore
    @Min(value = 1, message = "Customer id is needed")
    private int customer_id;

    @CreatedDate
    private LocalDate booked_date;

    private String Status;

    private String comments;

    private LocalDate confirm_date;

    @Min(value = 1, message = "service id is needed")
    private int service_id;

    private Services service;
}
