package com.avvsion.service.repository;

import com.avvsion.service.model.Services;
import com.avvsion.service.rowmappers.ServiceRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ServiceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int addService(Services service){

        String sql = "INSERT INTO services(seller_id, name, unit_price, image, message, category)" +
                "VALUES(?,?,?,?,?,?)";

        return jdbcTemplate.update(sql, service.getSeller_id(), service.getService_name(),
                service.getUnit_price(), service.getImage(), service.getMessage(), service.getCategory_name());
    }

    public List<Services> getAllServicesByCategory(String category){
        String sql = "SELECT * FROM services WHERE category = ?";

        try{
            return jdbcTemplate.query(sql,new ServiceRowMapperImpl(), category);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Services> getAllServices(){
        String sql = "SELECT * FROM services";

        try{
            return jdbcTemplate.query(sql,new ServiceRowMapperImpl());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int getSellerIdByServiceId(int service_id){
        String sql = "SELECT seller_id FROM services WHERE service_id = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("seller_id");
            }
        },service_id);
    }

    public Services getServiceById(int service_id){
        String sql = "SELECT * FROM services WHERE service_id = ?";
        return jdbcTemplate.queryForObject(sql,new ServiceRowMapperImpl(),service_id);
    }
}
