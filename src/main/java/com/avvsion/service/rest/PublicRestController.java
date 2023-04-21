package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.security.JwtAuthResponse;
import com.avvsion.service.security.JwtTokenHelper;
import com.avvsion.service.service.CustomerService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.SellerService;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/public/api", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PublicRestController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody AuthCredential authCredential,
                                                       HttpSession session) throws Exception {

        this.authenticate(authCredential.getEmail(), authCredential.getPass());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authCredential.getEmail());

        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        if(authCredential.getRole().equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = customerService.getCustomer(authCredential.getEmail());
            customer.getPerson().setPwd(null);
            response.setCustomer(customer);
            session.setAttribute("customerInfo", customer);
        }
        else if(AvServiceConstants.SELLER_ROLE.equals(authCredential.getRole())){
            Sellers seller = sellerService.getSellerDetails(authCredential.getEmail());
            seller.getPerson().setPwd(null);
            response.setSellers(seller);
            session.setAttribute("sellerInfo", seller);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/newCustomer")
    public ResponseEntity<Response> signUp(@Valid @RequestBody Customers customer){
        if(this.personService.checkEmailExistOrNot(customer.getPerson().getEmail())){
            return ResponseEntity.status(HttpStatus.IM_USED).body(new Response());
        }
        customerService.saveCustomer(customer);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @PostMapping("/newSeller")
    public ResponseEntity<Response> signUp(@Valid @RequestBody Sellers seller){
        if(this.personService.checkEmailExistOrNot(seller.getPerson().getEmail())){
            return ResponseEntity.status(HttpStatus.IM_USED).body(new Response());
        }
        sellerService.saveSeller(seller);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @PostMapping("/copyImage")
    public void copyImage(@RequestParam MultipartFile file){
        this.fileService.uploadImage(path,file);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<Response> checkEmail(@RequestParam String email){
        boolean res = this.personService.checkEmailExistOrNot(email);
        Response response = new Response();
        String success = "";
        if(res){
            response.setStatusMsg("Email is Already Exists !!");
            success = "false";
        }else{
            response.setStatusMsg("Email is not Exists :)");
            success = "true";
        }
        return ResponseEntity
                .status(HttpStatus.IM_USED)
                .header("isEmailExists", success)
                .body(response);
    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password !!");
        }

    }

}
