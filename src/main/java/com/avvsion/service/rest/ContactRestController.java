package com.avvsion.service.rest;

import com.avvsion.service.constants.AvServiceConstants;
import com.avvsion.service.model.ApiResponse;
import com.avvsion.service.model.Contact;
import com.avvsion.service.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "public/api/contact", produces =
        {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@CrossOrigin("*")
public class ContactRestController {

    @Autowired
    ContactService contactService;

    @GetMapping("/getAllMessagesByStatus")
    //@ResponseBody
    public List<Contact> getMessagesByStatus(@RequestParam String status) {

        if (status != null) {
            return  contactService.findMsgsWithOpenStatus(status);
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/saveMsg")
    // @ResponseBody
    public ResponseEntity<ApiResponse> saveMsg(@Valid @RequestBody Contact contact){
        contactService.saveMessageDetails(contact);
        ApiResponse response = new ApiResponse("Message saved successfully",true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @DeleteMapping("/deleteMsg")
   public ResponseEntity<ApiResponse> deleteMsg(RequestEntity<Contact> requestEntity){

       HttpHeaders headers = requestEntity.getHeaders();
       headers.forEach((key,value)-> log.info(String.format(
               "Header '%s' = %s", key, String.join("|", value))));

       //Contact contact = requestEntity.getBody();
//       contactService.deleteById(contact.getContact_id());
//       Response response = new Response();
//       response.setStatusMsg("Message successfully deleted");
//       response.setStatusCode("200");
//       return ResponseEntity
//               .status(HttpStatus.OK)
//               .body(response);
        return ResponseEntity.accepted().body(new ApiResponse());
   }

   @PatchMapping("/closeMsg")
    public ResponseEntity<ApiResponse> closeMsg(@RequestParam int contact_id){

        contactService.updateMsgStatus(contact_id,AvServiceConstants.CLOSE, AvServiceConstants.ADMIN_ROLE);
       /*else{
           response.setStatusCode("400");
           response.setStatusMsg("Invalid Contact Id received");
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(response);
       }*/
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(new ApiResponse("Message Successfully Closed",true));
   }
}
