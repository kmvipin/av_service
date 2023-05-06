package com.avvsion.service.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Payments {

    @JsonIgnore
    private int payment_id;

    @NotBlank(message = "payment_method is needed")
    private String payment_method;

    private Orders order;

    @CreatedDate
    private LocalDate date;

    private int amount;

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String razorpaySignature;
}
