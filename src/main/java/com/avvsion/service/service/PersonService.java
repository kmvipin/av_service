package com.avvsion.service.service;

import com.avvsion.service.model.Person;
import com.avvsion.service.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public int initializePerson(Person person){
        return personRepository.savePerson(person);
    }
    public Person getPersonDetails(String email){
        return personRepository.readByEmail(email);
    }
    public boolean checkEmailExistOrNot(String email){
        return personRepository.EmailExistOrNot(email);
    }
    public int getPersonId(String email){
        return personRepository.getPersonIdByEmail(email);
    }
    public int updateDetails(Person person){
        return personRepository.updatePersonById(person);
    }
    public String getRoleById(int id){
        return personRepository.getRoleById(id);
    }

    public Person getPersonById(int id){
        return personRepository.readById(id);
    }
}
