package com.avvsion.service.repository;

import com.avvsion.service.model.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EmployeeDao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addEmployee(Employees employee){
        String sql = "INSERT INTO employees (employee_id, person_id, job_title, salary, hire_date)" +
                "VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(sql,employee.getEmployee_id(), employee.getPerson_id(), employee.getJob_title(),
                employee.getSalary(), employee.getHire_date());
    }
}
