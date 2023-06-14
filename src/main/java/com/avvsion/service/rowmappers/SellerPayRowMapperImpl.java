package com.avvsion.service.rowmappers;

import com.avvsion.service.model.SellerPay;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SellerPayRowMapperImpl implements RowMapper<SellerPay> {
    @Override
    public SellerPay mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
