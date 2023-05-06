package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.*;
import com.avvsion.service.security.PersonDetailService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
@CrossOrigin("*")
public class PersonRestController {

    @Autowired
    private PersonService personService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private PersonDetailService userDetails;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logOut(HttpServletRequest request){
        httpServletRequest.getSession().invalidate();
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Logout Successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isLogout", "true")
                .body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam MultipartFile image,
                                                Authentication authentication, HttpSession session){
        List<GrantedAuthority> authority = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = authority.get(0).getAuthority();
        Person person = null;
        if(role.equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = (Customers) session.getAttribute("customerInfo");
            person = customer.getPerson();
        }
        else{
            Sellers seller = (Sellers) session.getAttribute("sellerInfo");
            person = seller.getPerson();
        }
        if(person != null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Already uploaded");
            apiResponse.setSuccess(false);
            return ResponseEntity.status(500).body(apiResponse);
        }
        person.setImage(this.fileService.uploadImage(path,image));
        personService.updateDetails(person);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("SuccessFully Uploaded");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isImageSaved", "true")
                .body(apiResponse);
    }

    @GetMapping(value = "/requestImage", produces =
            {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public void requestImage(HttpServletResponse response, HttpSession session,
                             Authentication authentication) throws IOException {
        List<GrantedAuthority> grantedAuthorities =
                (List<GrantedAuthority>) authentication.getAuthorities();
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

    @DeleteMapping("/deleteImage")
    public ResponseEntity<ApiResponse> deleteImage(HttpSession session,
                                                   Authentication authentication){
        List<GrantedAuthority> grantedAuthorities =
                (List<GrantedAuthority>) authentication.getAuthorities();
        String role = grantedAuthorities.get(0).getAuthority();
        Person person = null;
        if(role.equals(AvServiceConstants.CUSTOMER_ROLE)){
            Customers customer = (Customers) session.getAttribute("customerInfo");
            person = customer.getPerson();
        }
        else{
            Sellers seller = (Sellers) session.getAttribute("sellerInfo");
            person = seller.getPerson();
        }
        ApiResponse response = new ApiResponse();
        if(person.getImage() == null){
            response.setMessage("Image is unavailable");
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
        this.fileService.deleteImage(path,person.getImage());
        person.setImage(null);
        personService.updateDetails(person);

        response.setMessage("SuccessFully deleted");
        response.setSuccess(true);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/getPerson")
    public ResponseEntity<Person> getDetails(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        String username = ((UserDetails) principal).getUsername();
        Person person = personService.getPersonDetails(username);
        return ResponseEntity.status(200).body(person);
    }
}