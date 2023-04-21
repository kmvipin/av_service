package com.avvsion.service.rowmappers;

import com.avvsion.service.model.Address;
import com.avvsion.service.model.Person;
import com.avvsion.service.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PersonRowMapperImpl implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setPerson(resultSet.getString("first_name"), resultSet.getString("last_name"),
                resultSet.getString("phone"), resultSet.getString("email"), null,
                resultSet.getString("pwd"), null, resultSet.getString("gender"),
                null, new Role(""), new Address(), resultSet.getInt("age"));
        person.setPerson_id(resultSet.getInt("person_id"));
        LocalDate localDate = resultSet.getObject("date_of_birth", LocalDate.class);
        person.setDate_of_birth(localDate);
        person.getRoles().setRoleId(resultSet.getInt("role_id"));
        return person;
    }
}
