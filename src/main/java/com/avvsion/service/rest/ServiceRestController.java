package com.avvsion.service.rest;

import com.avvsion.service.model.ApiResponse;
import com.avvsion.service.model.Person;
import com.avvsion.service.model.Sellers;
import com.avvsion.service.model.Services;
import com.avvsion.service.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/service")
public class ServiceRestController {

    @Autowired
    private ServicesService servicesService;

    @PostMapping("/addService")
    public ResponseEntity<ApiResponse> addService(@Valid @RequestBody
                                                      Services service, Authentication authentication) {
        System.out.println(service+"jadgikjabhripgua");
        ApiResponse response = new ApiResponse();
        String flag = "false";
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        String username = ((UserDetails) principal).getUsername();
        if (servicesService.addService(service, username) == 0) {
            response.setSuccess(false);
            response.setMessage("User is not a Seller");
        } else {
            response.setSuccess(true);
            response.setMessage("service saved successfully");
            flag = "true";
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isServiceSaved", flag)
                .body(response);
    }

    @GetMapping("/getAllServices")
    public List<Services> getAllServices(){
        return servicesService.getAllServices();
    }

    @DeleteMapping("/removeService")
    public ResponseEntity<ApiResponse> removeService(int service_id){
        if(!servicesService.removeService(service_id)){
            return ResponseEntity.status(404).body(new ApiResponse("Service Not Found", false));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Service SuccessFully Deleted", true));
    }
}
