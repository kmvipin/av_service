package com.avvsion.service.rowmappers;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Address;
import com.avvsion.service.model.Person;
import com.avvsion.service.model.Role;
import com.avvsion.service.model.Sellers;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SellerRowMapperImpl implements RowMapper<Sellers> {

    @Override
    public Sellers mapRow(ResultSet resultSet, int i) throws SQLException {
        Sellers seller = new Sellers();
        Person person = new Person();
        Address address = new Address();
        seller.setSeller_id(resultSet.getInt("seller_id"));
        person.setPerson_id(resultSet.getInt("person_id"));
        person.setFirst_name(resultSet.getString("first_name"));
        person.setLast_name(resultSet.getString("last_name"));
        person.setImage(resultSet.getString("image"));
        person.setMobileNumber(resultSet.getString("phone"));
        person.setEmail(resultSet.getString("email"));
        person.setPwd(resultSet.getString("pwd"));
        person.setAge(resultSet.getInt("age"));
        person.setGender(resultSet.getString("gender"));
        address.setAddress_id(resultSet.getInt("address_id"));
        address.setAddress1(resultSet.getString("address1"));
        address.setAddress2(resultSet.getString("address2"));
        address.setCity(resultSet.getString("city"));
        address.setState(resultSet.getString("state"));
        address.setZipCode(resultSet.getString("zip_code"));
        LocalDate localDate = resultSet.getObject("date_of_birth", LocalDate.class);
        person.setDate_of_birth(localDate);
        person.setAddress(address);
        person.setRoles(new Role(AvServiceConstants.CUSTOMER_ROLE));
        person.getRoles().setRoleId(resultSet.getInt("role_id"));
        seller.setPerson(person);
        return seller;
    }
}
