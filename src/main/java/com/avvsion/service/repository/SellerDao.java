package com.avvsion.service.repository;

import com.avvsion.service.model.*;
import com.avvsion.service.rowmappers.CustomerRowMapperImpl;
import com.avvsion.service.rowmappers.SellerRowMapperImpl;
import com.avvsion.service.rowmappers.ServiceRowMapperImpl;
import com.avvsion.service.service.OrderService;
import com.avvsion.service.service.ServicesService;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.channels.SeekableByteChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SellerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SellerDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private OrderDao orderDao;

    public int addSeller(Sellers seller){

        String sql = "INSERT INTO sellers(seller_id)" +
                "VALUES (?)";

        return jdbcTemplate.update(sql, seller.getPerson().getPerson_id());
    }

    public Sellers getSellerById(Sellers seller){
        /*
            code if more fields in seller table
         */
        return seller;
    }

    public int updateSellerDetails(Sellers seller){
        /*
            code if extra fields in sellers table
         */
        return 1;
    }

    public List<Sellers> getAllSellerByRole(String role){
        String sql = "SELECT * FROM persons JOIN sellers ON persons.person_id = sellers.seller_id " +
                "JOIN address ON persons.person_id = address.address_id " +
                "JOIN roles ON persons.role_id = roles.role_id " +
                "WHERE roles.role_name = ?";

        return jdbcTemplate.query(sql,new SellerRowMapperImpl(), role);
    }

    public List<Services> getServices(int id){
        String sql = "SELECT * FROM services WHERE seller_id = ?";

        try{
            return jdbcTemplate.query(sql,new ServiceRowMapperImpl(), id);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int saveSellerPay(SellerPay sellerPay){
        String sql = "INSERT INTO seller_pay(seller_id,service_id,order_id,amount,_status) " +
                "VALUES(?,?,?,?,?)";

        return jdbcTemplate.update(sql,sellerPay.getSeller_id(),
                sellerPay.getOrder().getService_id(),
                sellerPay.getOrder().getOrder_id(),sellerPay.getAmount(),"PENDING");
    }

    public List<SellerPay> getSellerOrders(String category, int seller_id){
        String sql = "SELECT * FROM orders JOIN services ON orders.service_id = services.service_id "+
                        "WHERE services.seller_id = ? AND services.category = ?";

        try{
            return jdbcTemplate.query(sql, new RowMapper<SellerPay>() {
                @Override
                public SellerPay mapRow(ResultSet resultSet, int i) throws SQLException {
                    SellerPay sellerPay = new SellerPay();
                    sellerPay.getOrder().setOrder_id(resultSet.getInt("order_id"));
                    sellerPay.getOrder().setComments(resultSet.getString("comments"));
                    sellerPay.getOrder().setCustomer_id(resultSet.getInt("customer_id"));
                    sellerPay.getOrder().setStatus(resultSet.getString("status"));
                    LocalDate localDate = resultSet.getObject("booked_date", LocalDate.class);
                    sellerPay.getOrder().setBooked_date(localDate);
                    localDate = resultSet.getObject("confirm_date", LocalDate.class);
                    sellerPay.getOrder().setConfirm_date(localDate);
                    sellerPay.getOrder().setService_id(resultSet.getInt("service_id"));
                    Person person = personRepository.readById(resultSet.getInt("customer_id"));
                    Services service = servicesService.getService(resultSet.getInt("service_id"));
                    sellerPay.setPerson(person);
                    sellerPay.getOrder().setService(service);
                    return sellerPay;
                }
            }, seller_id, category);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<SellerPay> getSellerPay(int seller_id){
        System.out.println("askjdhkash");
        String sql = "SELECT * FROM seller_pay WHERE seller_id = ?";
        try{
            return jdbcTemplate.query(sql, new RowMapper<SellerPay>() {
                @Override
                public SellerPay mapRow(ResultSet resultSet, int i) throws SQLException {
                    SellerPay sellerPay = new SellerPay();
                    sellerPay.setSeller_id(resultSet.getInt("id"));
                    int service_id = resultSet.getInt("service_id");
                    System.out.println("service id : "+service_id);
                    sellerPay.setService(servicesService.getService(service_id));
                    int order_id = resultSet.getInt("order_id");
                    System.out.println("order_id : "+order_id);
                    sellerPay.setOrder(orderDao.getOrderById(order_id));
                    sellerPay.setAmount(resultSet.getInt("amount"));
                    sellerPay.setStatus(resultSet.getString("_status"));
                    sellerPay.setPerson(personRepository.readById(sellerPay.getOrder().getCustomer_id()));
                    System.out.println(sellerPay);
                    return sellerPay;
                }
            },seller_id);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
