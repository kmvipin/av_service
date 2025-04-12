package com.avvsion.service.service;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.repository.AddressDao;
import com.avvsion.service.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private AddressDao addressDao;

    @Autowired
    private PersonService personService;

    public int saveCustomer(Customers customer){
        Person person = customer.getPerson();
        if(person.getAddress() == null){
            person.setAddress(new Address());
        }
        person.setRoles(new Role(AvServiceConstants.CUSTOMER_ROLE));
        personService.initializePerson(person);
        Address address = customer.getPerson().getAddress();
        customerDao.addCustomer(customer);
        address.setAddress_id(person.getPerson_id());
        addressDao.saveAddress(address);
        return 1;
    }

    public Customers getCustomer(String email){

        Person person = personService.getPersonDetails(email);
        Customers customer = new Customers();
        customer.setPerson(person);
        customer.setCustomer_id(person.getPerson_id());
        customerDao.getCustomerById(customer);
        return customer;
    }
    public int updateCustomer(Customers customer){
        Person person = customer.getPerson();
        Address address = person.getAddress();
        person.setPerson_id(personService.getPersonId(person.getEmail()));
        personService.updateDetails(person);
        address.setAddress_id(person.getPerson_id());
        addressDao.updateAddress(address);
        return customerDao.updateCustomer(customer);
    }

    public List<Customers> getALlCustomers(){
        return customerDao.getAllCustomersByRole(AvServiceConstants.CUSTOMER_ROLE);
    }

    public List<Orders> getCustomerOrdersByCustomerId(int customer_id){
        return customerDao.getOrdersByCustomerId(customer_id);
    }
}
