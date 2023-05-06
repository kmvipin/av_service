package com.avvsion.service.model;

import com.avvsion.service.annotation.FieldsValueMatch;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.avvsion.service.annotation.StrongPassword;

import java.time.LocalDate;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "pwd",
                fieldMatch = "confirmPwd",
                message = "Password does not match"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message =  "Email address does not match"
        )
})
public class Person extends BaseEntity{

    @JsonIgnore
    private int person_id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, message = "Name must be at least 3 character")
    private String first_name;

    @Size(min = 3, message = "Name must be at least 3 character")
    private String last_name;

    private String image;

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Confirm Email must not be blank")
    @Email(message = "Please enter a valid email")
    private String confirmEmail;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, message = "Password must be in 5 digit")
    @StrongPassword
    private String pwd;

    @NotBlank(message = "Confirm Password must not be blank")
    @Size(min = 5, message = "Confirm Password must be in 5 digit")
    private String confirmPwd;

    //@Min(value = 12, message = "age must be above 12")
    private int age;

    //@Pattern(regexp = "^$|[MF]{1}", message = "Gender must be either 'M' or 'F'")
    private String gender;

   // @NotNull(message = "Date must not be blank")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Past(message = "date must be in past")
    private LocalDate date_of_birth;

    private Role roles;

    private Address address;

    public void setPerson(String firstName, String lastName, String phoneNumber,
                          String email, String confirmEmail, String pwd, String confirmPwd,
                          String gender, LocalDate dateOfBirth, Role role, Address address, int age){

        this.first_name = firstName;
        this.last_name = lastName;
        this.mobileNumber = phoneNumber;
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.pwd = pwd;
        this.confirmPwd = confirmPwd;
        this.gender = gender;
        this.date_of_birth = dateOfBirth;
        this.roles = role;
        this.address = address;
        this.age = age;
    }
}
