package com.avvsion.service.model;

import lombok.Data;

@Data
public class SellerPay {

    private int id;

    private Services service;

    private int seller_id;

    private Orders order;

    private int amount;

    private String status;

    private Person person;
}
