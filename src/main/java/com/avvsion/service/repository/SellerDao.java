package com.avvsion.service.repository;

import com.avvsion.service.model.Sellers;
import com.avvsion.service.model.Services;
import com.avvsion.service.rowmappers.CustomerRowMapperImpl;
import com.avvsion.service.rowmappers.SellerRowMapperImpl;
import com.avvsion.service.rowmappers.ServiceRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.channels.SeekableByteChannel;
import java.util.List;

@Repository
public class SellerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SellerDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
}
