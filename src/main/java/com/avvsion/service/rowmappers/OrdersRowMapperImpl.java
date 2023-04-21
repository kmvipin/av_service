package com.avvsion.service.rowmappers;

import com.avvsion.service.model.Address;
import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrdersRowMapperImpl implements RowMapper<Orders> {

    @Override
    public Orders mapRow(ResultSet resultSet, int i) throws SQLException {
        Orders order = new Orders();
        order.setOrder_id(resultSet.getInt("order_id"));
        order.setComments(resultSet.getString("comments"));
        order.setCustomer_id(resultSet.getInt("customer_id"));
        order.setStatus(resultSet.getString("status"));
        LocalDate localDate = resultSet.getObject("booked_date", LocalDate.class);
        order.setBooked_date(localDate);
        localDate = resultSet.getObject("confirm_date", LocalDate.class);
        order.setConfirm_date(localDate);
        order.setService_id(resultSet.getInt("service_id"));
        Person person = new Person();
        Address address = new Address();
        person.setMobileNumber(resultSet.getString("phone"));
        address.setAddress_id(resultSet.getInt("address_id"));
        address.setState(resultSet.getString("state"));
        address.setZipCode(resultSet.getString("zip_code"));
        address.setAddress1(resultSet.getString("address1"));
        address.setAddress2(resultSet.getString("address2"));
        address.setCity(resultSet.getString("city"));
        person.setAddress(address);
        order.setPerson(person);
        return order;
    }
}
