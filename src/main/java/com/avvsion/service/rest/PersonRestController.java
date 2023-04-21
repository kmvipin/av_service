package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.Customers;
import com.avvsion.service.model.Response;
import com.avvsion.service.model.Sellers;
import com.avvsion.service.service.CustomerService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.SellerService;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/api/person")
public class PersonRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private PersonService personService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/logout")
    public ResponseEntity<Response> logOut(HttpServletRequest request){
        httpServletRequest.getSession().invalidate();
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Logout Successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isLogout", "true")
                .body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadImage(@RequestParam MultipartFile image,
                                                Authentication authentication, HttpSession session){
        List<GrantedAuthority> authority = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = authority.get(0).getAuthority();
        if(role.equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = (Customers) session.getAttribute("customerInfo");
            customer.getPerson().setImage(this.fileService.uploadImage(path,image));
            personService.updateDetails(customer.getPerson());
        }
        else{
            Sellers seller = (Sellers) session.getAttribute("sellerInfo");
            seller.getPerson().setImage(this.fileService.uploadImage(path,image));
            personService.updateDetails(seller.getPerson());
        }
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Image Saved SuccessFullly");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isImageSaved", "true")
                .body(response);
    }

    @GetMapping(value = "/requestImage", produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public void requestImage(HttpServletResponse response, HttpSession session, Authentication authentication) throws IOException {
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = grantedAuthorities.get(0).getAuthority();
        InputStream resource = null;
        String name = null;
        if(role.equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = (Customers) session.getAttribute("customerInfo");
            name = customer.getPerson().getImage();
            if(name == null){
                return;
            }
            resource = this.fileService.requestImage(path,name);
        }
        else{
            Sellers seller = (Sellers) session.getAttribute("sellerInfo");
            name = seller.getPerson().getImage();
            if(name == null){
                return;
            }
            resource = this.fileService.requestImage(path,name);
        }
        if(name.substring(name.lastIndexOf(".")).equals(".png")){
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }else{
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        }
        StreamUtils.copy(resource,response.getOutputStream());
    }
}