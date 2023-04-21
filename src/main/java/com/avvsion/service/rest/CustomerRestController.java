package com.avvsion.service.rest;

import com.avvsion.service.model.*;
import com.avvsion.service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/customer", produces =
        {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getInfo")
    public Customers getUserDetails(Authentication authentication, HttpSession session){
        return (Customers) session.getAttribute("customerInfo");

    }

    @GetMapping("/getAllCustomers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Customers> getAllCustomers(){
        return customerService.getALlCustomers();
    }

}