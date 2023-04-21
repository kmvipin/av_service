package com.avvsion.service.repository;

import com.avvsion.service.model.Address;
import com.avvsion.service.model.Person;
import com.avvsion.service.rowmappers.AddressRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class AddressDao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AddressDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int saveAddress(Address address){
        String sql = "INSERT INTO address (address_id, address1, address2, city, state, zip_code)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        int status = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, address.getAddress_id());
                ps.setString(2, address.getAddress1());
                ps.setString(3, address.getAddress2());
                ps.setString(4, address.getCity());
                ps.setString(5, address.getState());
                ps.setString(6, address.getZipCode());
                return ps;
            }
        });
        return status;
    }

    public Address getAddressById(int addressId){
        String sql = "SELECT * FROM address WHERE address_id = ?";

        return jdbcTemplate.queryForObject(sql,new AddressRowMapperImpl(), addressId);
    }
    public int updateAddress(Address address){
        String sql = "UPDATE address SET address1 = ?, address2 = ?, city = ?, state = ?, zip_code = ? " +
                "WHERE address_id = ?";

        return jdbcTemplate.update(sql,address.getAddress1(), address.getAddress2(), address.getCity(),
                address.getState(), address.getZipCode(), address.getAddress_id());
    }
}
