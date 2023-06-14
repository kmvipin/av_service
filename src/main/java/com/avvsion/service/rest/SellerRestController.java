package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.service.OrderService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/seller", produces = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE})
public class SellerRestController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private PersonService personService;


    @GetMapping("/getInfo")
    public Sellers getSellerDetails(HttpSession session){
        Sellers seller = (Sellers)session.getAttribute("sellerInfo");
        return seller;
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<ApiResponse> updateByEmail(@Valid @RequestBody Sellers seller, HttpSession httpSession){
        sellerService.updateSellerDetails(seller);
        Sellers httpSeller = (Sellers) httpSession.getAttribute("sellerInfo");
        seller.setSeller_id(httpSeller.seller_id);
        seller.getPerson().setPerson_id(httpSeller.getSeller_id());
        seller.getPerson().setEmail(httpSeller.getPerson().getEmail());
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @GetMapping("/getAllSellers")
    public List<Sellers> getAllSellers(){
        return sellerService.getAllSeller();
    }

    @GetMapping("/getServices")
    public List<Services> getServices(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = authorities.get(0).getAuthority();
        if(!role.equals(AvServiceConstants.SELLER_ROLE)){
            throw new RuntimeException("Invalid User");
        }
        String username = ((UserDetails) principal).getUsername();
        int seller_id = personService.getPersonId(username);
        return sellerService.getServices(seller_id);
    }

    @GetMapping("/getSellerOrderByCategory")
    public List<SellerPay> getOrderByCategory(@RequestParam String category, HttpSession session){
        Sellers seller = (Sellers) session.getAttribute("sellerInfo");
        return sellerService.getSellerOrder(category,seller.getSeller_id());
    }

    @GetMapping("/getSellerBooking")
    public List<SellerPay> getSellerBooking(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = authorities.get(0).getAuthority();
        if(!role.equals(AvServiceConstants.SELLER_ROLE)){
            throw new RuntimeException("Invalid User");
        }
        String username = ((UserDetails) principal).getUsername();
        int seller_id = personService.getPersonId(username);
        System.out.println(seller_id);
        return sellerService.getSellerBooking(seller_id);
    }
}