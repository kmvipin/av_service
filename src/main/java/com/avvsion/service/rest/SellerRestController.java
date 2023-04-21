package com.avvsion.service.rest;

import com.avvsion.service.model.Response;
import com.avvsion.service.model.Sellers;
import com.avvsion.service.model.Services;
import com.avvsion.service.service.SellerService;
import jdk.nashorn.internal.runtime.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/seller", produces = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE})
public class SellerRestController {

    @Autowired
    private SellerService sellerService;


    @GetMapping("/getInfo")
    public Sellers getSellerDetails(@RequestParam String email){
        return sellerService.getSellerDetails(email);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<Response> updateByEmail(@Valid @RequestBody Sellers seller, HttpSession httpSession){
        sellerService.updateSellerDetails(seller);
        Sellers httpSeller = (Sellers) httpSession.getAttribute("sellerInfo");
        seller.setSeller_id(httpSeller.seller_id);
        seller.getPerson().setPerson_id(httpSeller.getSeller_id());
        seller.getPerson().setEmail(httpSeller.getPerson().getEmail());
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
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
    public List<Services> getServices(HttpSession session){
        Sellers sellers = (Sellers) session.getAttribute("sellerInfo");
        return sellerService.getServices(sellers.getSeller_id());
    }
}
