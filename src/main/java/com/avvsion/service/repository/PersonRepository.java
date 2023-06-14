package com.avvsion.service.repository;

import com.avvsion.service.model.AuthCredential;
import com.avvsion.service.model.Person;
import com.avvsion.service.model.Role;
import com.avvsion.service.rowmappers.PersonRowMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    public PersonRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int savePerson(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String roleSql = "INSERT INTO roles(role_name)" +
                "VALUES(?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(roleSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, person.getRoles().getRoleName());
                return ps;
            }
        }, keyHolder);
       person.getRoles().setRoleId(keyHolder.getKey().intValue());
       String sql = "INSERT INTO persons(first_name, last_name, email, phone, pwd, role_id," +
               " age, date_of_birth, gender, created_at, created_by, image)" +
               "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        int status = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, person.getFirst_name());
                ps.setString(2, person.getLast_name());
                ps.setString(3, person.getEmail());
                ps.setString(4, person.getMobileNumber());
                ps.setString(5, person.getPwd());
                ps.setInt(6, person.getRoles().getRoleId());
                ps.setInt(7, person.getAge());
                if(person.getDate_of_birth() != null) {
                    ps.setDate(8, Date.valueOf(person.getDate_of_birth()));
                }
                else{
                    ps.setDate(8, null);
                }
                ps.setString(9, person.getGender());
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(11, person.getFirst_name());
                ps.setString(12, person.getImage());
                return ps;
            }
        }, keyHolder);
                person.setPerson_id(keyHolder.getKey().intValue());
        return 1;
    }
    public Person readByEmail(String email){
        String sql = "SELECT * FROM persons WHERE email = ?";
        Person person = jdbcTemplate.queryForObject(sql,new PersonRowMapperImpl(),email);
        getRole(person);
        person.setAddress(addressDao.getAddressById(person.getPerson_id()));
        return person;
    }

    public Person readById(int id) {
        String sql = "SELECT * FROM persons WHERE person_id = ?";

        Person person = jdbcTemplate.queryForObject(sql,new PersonRowMapperImpl(),id);
        getRole(person);
        person.setAddress(addressDao.getAddressById(person.getPerson_id()));
        return person;
    }
    private Role getRole(Person person){
        String roleSql = "SELECT * FROM roles WHERE role_id = ?";
        Role role = jdbcTemplate.queryForObject(roleSql, new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Role(resultSet.getString("role_name"));
            }
        }, person.getRoles().getRoleId());

        person.getRoles().setRoleName(role.getRoleName());

        return role;
    }

    public int getPersonIdByEmail(String email){
        String sql = "SELECT person_id FROM persons WHERE email = ?";

         return jdbcTemplate.queryForObject(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("person_id");
            }
        }, email);
    }

    public int updatePersonById(Person person){
        String sql = "UPDATE persons SET first_name = ?, last_name = ?, phone = ?," +
                "age = ?, date_of_birth = ?, gender = ?, updated_at = ?, updated_by = ? WHERE person_id = ?";
        Date dateOfBirth;
        if(person.getDate_of_birth() != null) {
            dateOfBirth = Date.valueOf(person.getDate_of_birth());
        }
        else{
            dateOfBirth = null;
        }
        return jdbcTemplate.update(sql, person.getFirst_name(), person.getLast_name(),
                person.getMobileNumber(), person.getAge(), dateOfBirth,
                person.getGender(), Timestamp.valueOf(LocalDateTime.now()), person.getFirst_name(),person.getPerson_id());
    }

    public String getRoleById(int id){
        String sql = "SELECT * FROM roles JOIN persons ON roles.role_id = persons.role_id " +
                "WHERE persons.person_id = ?";

        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        },id);
    }

    public AuthCredential getCredentialByEmail(String email){
        String sql = "SELECT persons.email, persons.pwd, roles.role_name FROM persons JOIN roles ON persons.role_id = roles.role_id " +
                "WHERE persons.email = ?";

        return jdbcTemplate.queryForObject(sql, new RowMapper<AuthCredential>() {
            @Override
            public AuthCredential mapRow(ResultSet resultSet, int i) throws SQLException {
                AuthCredential authCredential1 = new AuthCredential();
                authCredential1.setEmail(resultSet.getString("persons.email"));
                authCredential1.setPass(resultSet.getString("persons.pwd"));
                authCredential1.setRole(resultSet.getString("roles.role_name"));
                return authCredential1;
            }
        },email);
    }

    public boolean EmailExistOrNot(String email){
        String sql = "SELECT person_id FROM persons WHERE email = ?";
        try{
            jdbcTemplate.queryForObject(sql,Integer.class, email);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            return false;
        }
    }

    public boolean updateImage(int id, String image){
        String update = "UPDATE persons SET image = ? WHERE person_id = ?";
        try{
            jdbcTemplate.update(update,image,id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public String getEmailByPhone(String phoneNumber){
        String sql = "SELECT email FROM persons WHERE phone = ?";

        try{
            return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("email");
                }
            },phoneNumber);
        }
        catch (Exception e){
            return null;
        }
    }

    public boolean changePass(String phoneNumber, String newPass){
        String sql = "UPDATE persons SET pwd = ? WHERE phone = ?";

        try{
            jdbcTemplate.update(sql,newPass,phoneNumber);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
