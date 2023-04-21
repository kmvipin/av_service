package com.avvsion.service.rowmappers;

import com.avvsion.service.model.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressRowMapperImpl implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet resultSet, int i) throws SQLException {
        Address address = new Address();
        address.setAddress_id(resultSet.getInt("address_id"));
        address.setAddress1(resultSet.getString("address1"));
        address.setAddress2(resultSet.getString("address2"));
        address.setCity(resultSet.getString("city"));
        address.setState(resultSet.getString("state"));
        address.setZipCode(resultSet.getString("zip_code"));

        return address;
    }
}
