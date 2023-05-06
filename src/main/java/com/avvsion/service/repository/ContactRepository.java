package com.avvsion.service.repository;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Contact;
import com.avvsion.service.rowmappers.ContactRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContactRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveContactMsg(Contact contact){

        String sql = "INSERT INTO CONTACT_MSG (NAME, MOBILE_NUM, EMAIL, SUBJECT, MESSAGE, STATUS) " +
                "VALUES (?,?,?,?,?,?)";

        return jdbcTemplate.update(sql,contact.getName(),contact.getMobileNum(),
                contact.getEmail(),contact.getSubject(),contact.getMessage(), contact.getStatus());
    }

    public List<Contact> findMsgsWithStatus(String status){
        String sql = "SELECT * FROM CONTACT_MSG WHERE STATUS = ?";
        return jdbcTemplate.query(sql,new PreparedStatementSetter(){
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1,status);
            }
        },new ContactRowMapper());
    }

    public int updateMsgStatus(int contact_id, String status, String updatedBy){
        String sql = "UPDATE CONTACT_MSG SET STATUS = ? WHERE CONTACT_ID = ?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,status);
                ps.setString(2, String.valueOf(contact_id));
            }
        });
    }
}
