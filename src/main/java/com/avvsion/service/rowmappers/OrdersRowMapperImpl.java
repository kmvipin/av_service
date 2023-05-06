package com.avvsion.service.rowmappers;

import com.avvsion.service.model.Address;
import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Person;
import com.avvsion.service.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return order;
    }
}
