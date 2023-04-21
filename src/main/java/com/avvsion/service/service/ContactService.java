package com.avvsion.service.service;

import com.avvsion.service.ServiceApplication;
import com.avvsion.service.model.Contact;
import com.avvsion.service.repository.ContactDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactDao contactDao;
    public void saveMessage(Contact contact){
        System.out.println(contact);
        System.out.println(contactDao);
        contactDao.saveContactMsg(contact);
    }
}
