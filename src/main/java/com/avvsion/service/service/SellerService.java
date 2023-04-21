package com.avvsion.service.service;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.repository.AddressDao;
import com.avvsion.service.repository.PersonRepository;
import com.avvsion.service.repository.SellerDao;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService{
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private PersonService personService;

    @Autowired
    private AddressDao addressDao;

    public int saveSeller(Sellers seller){
        Person person = seller.getPerson();
        person.setRoles(new Role("Seller"));
        Address address = person.getAddress();
        if(personService.initializePerson(person) == 0){
            return 0;
        }
        sellerDao.addSeller(seller);
        address.setAddress_id(person.getPerson_id());
        addressDao.saveAddress(address);
        return 1;
    }

    public Sellers getSellerDetails(String email){
        Person person = personService.getPersonDetails(email);
        Sellers seller = new Sellers();
        seller.setPerson(person);
        seller.setSeller_id(person.getPerson_id());
        sellerDao.getSellerById(seller);
        return seller;
    }

    public int updateSellerDetails(Sellers seller){
        Person person = seller.getPerson();
        Address address = person.getAddress();
        personService.updateDetails(person);
        address.setAddress_id(person.getPerson_id());
        addressDao.updateAddress(address);
        return sellerDao.updateSellerDetails(seller);
    }

    public List<Sellers> getAllSeller(){
        return sellerDao.getAllSellerByRole(AvServiceConstants.SELLER_ROLE);
    }

    public List<Services> getServices(int id){
        return sellerDao.getServices(id);
    }
}