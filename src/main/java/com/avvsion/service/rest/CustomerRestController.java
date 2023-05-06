package com.avvsion.service.rest;

import com.avvsion.service.model.*;
import com.avvsion.service.repository.PersonRepository;
import com.avvsion.service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;
@RestController
@RequestMapping(value = "/api/customer", produces =
        {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@CrossOrigin("*")
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PersonRepository personRepository;
    @GetMapping("/getInfo")
    public Customers getUserDetails(Authentication authentication, HttpSession session){
        Customers customer = (Customers) session.getAttribute("customerInfo");
        System.out.println(customer);
        return customer;
    }
    @GetMapping("/getAllCustomers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Customers> getAllCustomers(){
        return customerService.getALlCustomers();
    }

    @GetMapping("/getCustomerOrders")
    public List<Orders> getCustomerOrders(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        String email = ((UserDetails) principal).getUsername();
        int customer_id = personRepository.getPersonIdByEmail(email);
        return customerService.getCustomerOrdersByCustomerId(customer_id);
    }
}