package com.avvsion.service.service;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Contact;
import com.avvsion.service.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Component
public class ContactService {

    @Autowired
    ContactRepository contactRepository;
    public boolean saveMessageDetails(Contact contact){

        boolean isSaved = true;
        contact.setStatus(AvServiceConstants.OPEN);
        contact.setCreatedAt(LocalDateTime.now());
        int result = contactRepository.saveContactMsg(contact);
        if(result > 0){
            isSaved = true;
        }
        return isSaved;
    }

    public List<Contact> findMsgsWithOpenStatus(String status){
        List<Contact> contactMsgs = contactRepository.findMsgsWithStatus(status);
        return contactMsgs;
    }

    public boolean updateMsgStatus(int contact_id, String status, String updatedBy){
        boolean isUpdated = false;
        int result  = contactRepository.updateMsgStatus(contact_id,status,updatedBy);
        if(result > 0){
            isUpdated = true;
        }
        return isUpdated;
    }
}
