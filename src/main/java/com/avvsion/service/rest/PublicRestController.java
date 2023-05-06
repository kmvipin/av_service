package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.model.ApiResponse;
import com.avvsion.service.model.JwtAuthResponse;
import com.avvsion.service.security.JwtTokenHelper;
import com.avvsion.service.service.CustomerService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.SellerService;
import com.avvsion.service.service.ServicesService;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/public/api", produces = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE})
@CrossOrigin("*")
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
    private ServicesService servicesService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/user_login")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody AuthCredential authCredential,
                                           HttpSession session) {

        try{
            this.authenticate(authCredential.getEmail(), authCredential.getPass());
        }
        catch (Exception e){
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setSuccess(false);
            jwtAuthResponse.setMessage("Invalid User");
            return ResponseEntity.status(404).body(jwtAuthResponse);
        }
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authCredential.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        if(authCredential.getRole().equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = customerService.getCustomer(authCredential.getEmail());
            customer.getPerson().setPwd(null);
            System.out.println("asgasgga"+customer);
            session.setAttribute("customerInfo", customer);
        }
        else if(AvServiceConstants.SELLER_ROLE.equals(authCredential.getRole())){
            Sellers seller = sellerService.getSellerDetails(authCredential.getEmail());
            seller.getPerson().setPwd(null);
            session.setAttribute("sellerInfo", seller);
        }
        response.setSuccess(true);
        response.setMessage("Login SuccessFully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/newCustomer")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody Customers customer){
        System.out.println(customer);
        if(!customer.getPerson().getPwd().equals(customer.getPerson().getConfirmPwd())){
            throw new RuntimeException("Password does not match");
        }
        if(!customer.getPerson().getEmail().equals(customer.getPerson().getConfirmEmail())){
            throw new RuntimeException("Email Does Not Match");
        }
        if(this.personService.checkEmailExistOrNot(customer.getPerson().getEmail())){
            return ResponseEntity.status(HttpStatus.IM_USED).body(new ApiResponse("Email Already Exist",false));
        }

        customerService.saveCustomer(customer);
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @PostMapping("/newSeller")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody Sellers seller){
        if(!seller.getPerson().getPwd().equals(seller.getPerson().getConfirmPwd())){
            throw new RuntimeException("Password does not match");
        }
        if(!seller.getPerson().getEmail().equals(seller.getPerson().getConfirmEmail())){
            throw new RuntimeException("Email Does Not Match");
        }
        if(this.personService.checkEmailExistOrNot(seller.getPerson().getEmail())){
            return ResponseEntity.status(HttpStatus.IM_USED).body(new ApiResponse("Email Already Exist",false));
        }
        if(this.personService.checkEmailExistOrNot(seller.getPerson().getEmail())){
            return ResponseEntity.status(HttpStatus.IM_USED).body(new ApiResponse());
        }
        sellerService.saveSeller(seller);
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @GetMapping("/getAllServicesByCategory")
    public List<Services> getAllServicesByCategory(@RequestParam String category){
        System.out.println("helloooooo");
        return servicesService.getAllServicesByCategory(category);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<ApiResponse> checkEmail(@RequestParam String email){
        boolean res = this.personService.checkEmailExistOrNot(email);
        ApiResponse response = new ApiResponse();
        String success = "";
        if(res){
            response.setMessage("Email is Already Exists !!");
            response.setSuccess(false);
            success = "false";
        }else{
            response.setMessage("Email is not Exists :)");
            response.setSuccess(true);
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
