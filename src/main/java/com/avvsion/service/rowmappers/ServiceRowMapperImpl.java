package com.avvsion.service.rowmappers;

import com.avvsion.service.model.Services;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceRowMapperImpl implements RowMapper<Services> {
    @Override
    public Services mapRow(ResultSet resultSet, int i) throws SQLException {
        Services service = new Services();
        service.setSeller_id(resultSet.getInt("seller_id"));
        service.setService_id(resultSet.getInt("service_id"));
        service.setService_name(resultSet.getString("name"));
        service.setImage(resultSet.getString("image"));
        service.setCategory_name(resultSet.getString("category"));
        service.setMessage(resultSet.getString("message"));
        service.setUnit_price(resultSet.getInt("unit_price"));
        return service;
    }
}
