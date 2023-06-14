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
    public boolean updateImage(int id, String image){
        return personRepository.updateImage(id,image);
    }

    public Person getPersonById(int id){
        return personRepository.readById(id);
    }

    public String getEmailByPhoneNumber(String phoneNumber){

        return personRepository.getEmailByPhone(phoneNumber);
    }

    public boolean changePass(String phoneNumber, String newPass){
        return personRepository.changePass(phoneNumber,newPass);
    }
}
