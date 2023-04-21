package com.avvsion.service.repository;

import com.avvsion.service.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveContactMsg(Contact contact){

        System.out.println("Name : "+contact.getName());
        String sql = "INSERT INTO CONTACT_MSG (contact_id, person_id, name, mobile_num, email, subject, message, status) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,contact.getContact_id(),contact.getPerson_id(),contact.getName(),contact.getMobileNum(),
                contact.getEmail(),contact.getSubject(),contact.getMessage(),
                contact.getStatus());
    }

    public List<Contact> findMsgsWithStatus(String status){
        String sql = "SELECT * FROM CONTACT_MSG WHERE STATUS = ?";
        //return jdbcTemplate.query(sql,"ACTIVE",new ContactRowMapper());
        return new ArrayList<>();
    }

    public int updateMsgStatus(int contact_id, String status, String updatedBy){
        String sql = "UPDATE CONTACT_MSG SET STATUS = ? WHERE CONTACT_ID = ?";

        return jdbcTemplate.update(sql,status,contact_id);
    }
}
