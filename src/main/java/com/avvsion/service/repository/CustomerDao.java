package com.avvsion.service.repository;

import com.avvsion.service.model.Customers;
import com.avvsion.service.model.Person;
import com.avvsion.service.rowmappers.CustomerRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CustomerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    public CustomerDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int addCustomer(Customers customer){

        String sql = "INSERT INTO customers (customer_id)" +
                "VALUES(?)";

        return jdbcTemplate.update(sql,customer.getPerson().getPerson_id());
    }

    public Customers getCustomerById(Customers customer) {

        /*
            code if extra fields in customer table
         */
        return customer;
    }

    public int updateCustomer(Customers customer){
        /*
            code if extra fields in customer
         */
        return 1;
    }

    public List<Customers> getAllCustomersByRole(String role){
        String sql = "SELECT * FROM persons JOIN customers ON persons.person_id = customers.customer_id " +
                "JOIN address ON persons.person_id = address.address_id " +
                "JOIN roles ON persons.role_id = roles.role_id " +
                "WHERE roles.role_name = ?";

        return jdbcTemplate.query(sql,new CustomerRowMapperImpl(), role);
    }
}
