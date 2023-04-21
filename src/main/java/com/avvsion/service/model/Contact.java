package com.avvsion.service.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
public class Contact {
    public Contact(int contact_id, String name, String mobileNum, String email, String subject, String message, String status) {
        this.contact_id = contact_id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private int contact_id;

    private int person_id;

    @NotBlank(message = "Field Must Be Required")
    @Size(min = 2,message = "name must be atleast 3 character")
    private String name;

    @NotBlank(message = "Field Must Be Required")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Please enter correct mobile number")
    private String mobileNum;

    @NotBlank(message = "Field Must Be Required")
    @Email(message = "Please enter valid email")
    private String email;

    @NotBlank(message = "Field Must Be Required")
    @Size(min = 5,message = "Please enter atleast 5 character subject")
    private String subject;

    @NotBlank(message = "Field Must Be Required")
    @Size(min = 10, message = "Please enter atleast 10 character")
    private String message;

    private String status;
}
