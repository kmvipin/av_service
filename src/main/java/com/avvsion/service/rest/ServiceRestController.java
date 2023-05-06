package com.avvsion.service.rest;

import com.avvsion.service.model.ApiResponse;
import com.avvsion.service.model.Sellers;
import com.avvsion.service.model.Services;
import com.avvsion.service.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                                                      Services service, HttpSession session) {
        ApiResponse response = new ApiResponse();
        String flag = "false";
        Sellers seller = (Sellers) session.getAttribute("sellerInfo");
        String email = seller.getPerson().getEmail();
        if (servicesService.addService(service, email) == 0) {
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
}
