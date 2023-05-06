package com.avvsion.service.repository;

import com.avvsion.service.model.Customers;
import com.avvsion.service.model.Orders;
import com.avvsion.service.rowmappers.CustomerRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CustomerDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ServiceDao serviceDao;

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

    public List<Orders> getOrdersByCustomerId(int customer_id){
        String sql = "SELECT * FROM orders "+
                "WHERE orders.customer_id = ?";

        try{
            return jdbcTemplate.query(sql, new RowMapper<Orders>() {
                @Override
                public Orders mapRow(ResultSet resultSet, int i) throws SQLException {
                    Orders order = new Orders();
                    order.setOrder_id(resultSet.getInt("order_id"));
                    order.setCustomer_id(resultSet.getInt("customer_id"));
                    order.setStatus(resultSet.getString("status"));
                    order.setComments(resultSet.getString("comments"));
                    LocalDate localDate = resultSet.getObject("booked_date", LocalDate.class);
                    order.setBooked_date(localDate);
                    LocalDate localDate1 = resultSet.getObject("confirm_date", LocalDate.class);
                    order.setConfirm_date(localDate1);
                    order.setService(serviceDao.getServiceById(resultSet.getInt("service_id")));
                    return order;
                }
            },customer_id);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
