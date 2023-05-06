package com.avvsion.service.repository;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Payments;
import com.avvsion.service.model.SellerPay;
import com.avvsion.service.rowmappers.OrdersRowMapperImpl;
import com.razorpay.Order;
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
    public Payments placeOrder(Payments payment){
        Orders order =payment.getOrder();
        String sql = "INSERT orders(customer_id, booked_date," +
                " status, comments, service_id) " +
                "VALUES(?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement
                    (Connection connection) throws SQLException {
                PreparedStatement ps = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,order.getCustomer_id());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, AvServiceConstants.ON_WAY);
                ps.setString(4, order.getComments());
                ps.setInt(5,order.getService_id());
                return ps;
            }
        },keyHolder);
        order.setOrder_id(keyHolder.getKey().intValue());
        savePayment(payment);
        return payment;
    }

    public int savePayment(Payments payments){
        String paymentSql = "INSERT payments (customer_id, date, " +
                "amount, payment_method, order_id,razorpay_payment_id,razorpay_order_id,razorpay_signature) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int var = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement
                    (Connection connection) throws SQLException {
                PreparedStatement ps = connection
                        .prepareStatement(paymentSql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,payments.getOrder().getCustomer_id());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(3,payments.getAmount());
                ps.setString(4, payments.getPayment_method());
                ps.setInt(5,payments.getOrder().getOrder_id());
                ps.setString(6, payments.getRazorpayPaymentId());
                ps.setString(7,payments.getRazorpayOrderId());
                ps.setString(8, payments.getRazorpaySignature());
                return ps;
            }
        },keyHolder);
        payments.setPayment_id(keyHolder.getKey().intValue());
        return 1;
    }
    public List<Orders> getOrdersByServiceId(int service_id){
        String sql = "SELECT * FROM orders "+
                "WHERE service_id = ?";
        return jdbcTemplate.query(sql, new OrdersRowMapperImpl(), service_id);
    }

    public int updateStatus(int order_id){
        String sql = "UPDATE orders SET status = 'CONFIRM' WHERE order_id = ?";
        return jdbcTemplate.update(sql,order_id);
    }

    public Orders getOrderById(int id){
        String sql = "SELECT * FROM orders "+
                "WHERE order_id = ?";

        return jdbcTemplate.queryForObject(sql, new OrdersRowMapperImpl(), id);
    }

}
