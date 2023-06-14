package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.model.ApiResponse;
import com.avvsion.service.model.JwtAuthResponse;
import com.avvsion.service.security.JwtTokenHelper;
import com.avvsion.service.service.*;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    private OTPGenerator otpGenerator;

    @Autowired
    private SMSService smsService;

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
        List<GrantedAuthority> auth = (List<GrantedAuthority>) userDetails.getAuthorities();
        if(!auth.get(0).getAuthority().equalsIgnoreCase(authCredential.getRole())){
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setSuccess(false);
            jwtAuthResponse.setMessage("Invalid User");
            return ResponseEntity.status(404).body(jwtAuthResponse);
        }
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        if(authCredential.getRole().equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = customerService.getCustomer(authCredential.getEmail());
            customer.getPerson().setPwd(null);
            System.out.println("asgasgga"+customer);
            response.setRole("CUSTOMER");
            session.setAttribute("customerInfo", customer);
        }
        else if(AvServiceConstants.SELLER_ROLE.equals(authCredential.getRole())){
            Sellers seller = sellerService.getSellerDetails(authCredential.getEmail());
            seller.getPerson().setPwd(null);
            session.setAttribute("sellerInfo", seller);
            response.setRole("SELLER");
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

        List<Services> services = servicesService.getAllServicesByCategory(category);
        return services;
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

    @PostMapping("/sendOTP")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String phoneNumber, HttpSession session){
        if(personService.getEmailByPhoneNumber(phoneNumber) == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Phone Number Must Be Valid", false));
        }
        String otp = otpGenerator.generateOTP();
        session.setAttribute("verifyOTP_"+phoneNumber,otp);
        smsService.sendOTP("+91"+phoneNumber, otp);

        return ResponseEntity.status(200).body(new ApiResponse(otp, true));
    }

    @GetMapping("/verifyOTP")
    public ResponseEntity<ApiResponse> verifyOTP(@RequestParam String otp, @RequestParam String email, HttpServletRequest request){
        String generatedOTP = (String)request.getSession().getAttribute("verifyOTP_"+email);
        if(!otp.equals(generatedOTP)){
            return ResponseEntity.status(400).body(new ApiResponse("Invalid OTP",false));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Verified Successfully",true));
    }

    @PostMapping("/changePass")
    public ResponseEntity<ApiResponse> changePassByPhoneNumber
            (@RequestParam String otp, @RequestParam String phoneNumber, @RequestParam String newPass, @RequestParam(value = "confirmPass") String confirmPass, HttpSession session){
            //String generatedOTP = (String)session.getAttribute("verifyOTP_"+email);
//            if(!otp.equals(generatedOTP)){
//                return ResponseEntity.status(400).body(new ApiResponse("Invalid OTP",false));
//            }
            if(confirmPass == null || confirmPass.length() <= 4){
                return ResponseEntity.status(400).body(new ApiResponse("Check Your Password",false));
            }

            if(!newPass.equals(confirmPass)){
                return ResponseEntity.status(400).body(new ApiResponse("New Pass And Confirm Pass Must be Same",false));
            }

            if(!personService.changePass(phoneNumber,confirmPass)){
                return ResponseEntity.status(400).body(new ApiResponse("Something went wrong",false));
            }
            return ResponseEntity.status(200).body(new ApiResponse("Password Change Successfully",true));
    }

    @GetMapping("/getEmailByPhone")
    public String getEmailByPhone(@RequestParam String phoneNumber, HttpServletRequest request){
        request.getCookies();
        if(phoneNumber == null || phoneNumber.length() > 10){
            throw new RuntimeException("Invalid Number");
        }

        String email = personService.getEmailByPhoneNumber(phoneNumber);
        if(email == null){
            throw new RuntimeException("Invalid Number");
        }
        JSONObject object = new JSONObject();
        object.put("email",email);
        return object.toString();
    }

}
