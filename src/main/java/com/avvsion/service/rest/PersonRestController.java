package com.avvsion.service.rest;

import com.avvsion.service.model.*;
import com.avvsion.service.security.PersonDetailService;
import com.avvsion.service.service.PersonService;
import com.avvsion.service.service.fileserviceimpl.FileServiceImpl;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StreamUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/api/person")
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
    public ResponseEntity<ApiResponse> logOut(HttpServletResponse res){
        Cookie invalidatedCookie = new Cookie("JSESSIONID", null);

        // Set the max age of the cookie to 0 to invalidate it
        invalidatedCookie.setMaxAge(0);

        // Set the cookie's path to match the original cookie's path
        invalidatedCookie.setPath("/");

        // Add the cookie to the response
        res.addCookie(invalidatedCookie);

        httpServletRequest.getSession().invalidate();
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Logout Successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isLogout", "true")
                .body(response);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("image") MultipartFile image,
                                                Authentication authentication, HttpSession session){
        System.out.println("hello guysss");
        System.out.println(image);
        Person person = (Person)session.getAttribute("personInfo");

        if(person.getImage() != null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Already uploaded");
            apiResponse.setSuccess(false);
            return ResponseEntity.status(500).body(apiResponse);
        }
        person.setImage(this.fileService.uploadImage(path,image));
        session.setAttribute("personInfo",person);
        if(!personService.updateImage(person.getPerson_id(),person.getImage())){
            return ResponseEntity.status(400).body(new ApiResponse("Failed",false));
        }

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

    public void requestImage(HttpServletResponse response,
                             Authentication authentication, HttpSession session) throws IOException {
        String name = null;
        InputStream resource = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        String username = ((UserDetails) principal).getUsername();
        Person person = personService.getPersonDetails(username);
//        Person person = (Person)session.getAttribute("personInfo");
        name = person.getImage();
        if(name == null){
            throw new RuntimeException("Image is null");
        }
        resource = this.fileService.requestImage(path,name);
        if(name.substring(name.lastIndexOf(".")).equals(".png")){
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }else{
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        }
        StreamUtils.copy(resource,response.getOutputStream());
        resource.close();
    }

    @DeleteMapping("/deleteImage")
    public ResponseEntity<ApiResponse> deleteImage(HttpSession session,
                                                   Authentication authentication){
        Person person = (Person)session.getAttribute("personInfo");
        ApiResponse response = new ApiResponse();
        if(person.getImage() == null){
            response.setMessage("Image is unavailable");
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
        this.fileService.deleteImage(path,person.getImage());
        person.setImage(null);
        session.setAttribute("personInfo",person);
        personService.updateImage(person.getPerson_id(),null);
        response.setMessage("SuccessFully deleted");
        response.setSuccess(true);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/getPerson")
    public ResponseEntity<Person> getDetails(HttpSession session,Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false) {
            throw new RuntimeException("User Not Exist");
        }
        String username = ((UserDetails) principal).getUsername();
        Person person = personService.getPersonDetails(username);
//        Person person = (Person)session.getAttribute("personInfo");
        return ResponseEntity.status(200).body(person);
    }


}