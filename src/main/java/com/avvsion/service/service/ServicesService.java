package com.avvsion.service.service;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Services;
import com.avvsion.service.repository.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ServicesService {
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private PersonService personService;
    public int addService(Services service, String email){

        if(service.getSeller_id() == 0){
            service.setSeller_id(personService.getPersonId(email));
        }
        if(!personService.getRoleById(service.getSeller_id()).equals(AvServiceConstants.SELLER_ROLE)){
            return 0;
        }
        if(serviceDao.addService(service) == 0){
            return 0;
        }
        return 1;
    }
    public List<Services> getAllServicesByCategory(String category){
        return serviceDao.getAllServicesByCategory(category);
    }
    public List<Services> getAllServices(){
        return serviceDao.getAllServices();
    }

    public Services getService(int service_id){
        return serviceDao.getServiceById(service_id);
    }

    public boolean removeService(int service_id){
        return serviceDao.removeService(service_id);
    }
}
