package com.avvsion.service.repository;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Payments;
import com.avvsion.service.rowmappers.OrdersRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int placeOrder(Payments payment){
        Orders order =payment.getOrder();
        String sql = "INSERT orders(customer_id, booked_date, status, comments, service_id) " +
                "VALUES(?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,order.getCustomer_id());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, AvServiceConstants.ON_WAY);
                ps.setString(4, order.getComments());
                ps.setInt(5,order.getService_id());
                return ps;
            }
        },keyHolder);

        order.setOrder_id(keyHolder.getKey().intValue());
        String paymentSql = "INSERT payments (customer_id, date, amount, payment_method, order_id) " +
                "VALUES (?,?,?,?,?)";

        return jdbcTemplate.update(paymentSql, order.getCustomer_id(), Timestamp.valueOf(LocalDateTime.now()),
                payment.getAmount(), payment.getPayment_method(), order.getOrder_id());
    }

    public List<Orders> getOrdersByServiceId(int service_id){
        String sql = "SELECT * FROM orders JOIN persons ON orders.customer_id = persons.person_id " +
                "JOIN address ON persons.person_id = address.address_id " +
                "WHERE service_id = ?";

        return jdbcTemplate.query(sql, new OrdersRowMapperImpl(), service_id);
    }

}
