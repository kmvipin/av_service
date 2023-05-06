package com.avvsion.service.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Contact extends BaseEntity{
    public Contact(int contact_id, String name, String mobileNum,
                   String email, String subject, String message, String status) {
        this.contact_id = contact_id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.status = status;
    }
    public Contact(){

    }

    private int contact_id;

    @JsonIgnore
    private int person_id;

    private String name;

    @NotBlank(message = "Field Must Be Required")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Please enter correct mobile number")
    private String mobileNum;

    private String email;

    private String subject;

    @NotBlank(message = "Field Must Be Required")
    @Size(min = 10, message = "Please enter atleast 10 character")
    private String message;

    private String status;
}
