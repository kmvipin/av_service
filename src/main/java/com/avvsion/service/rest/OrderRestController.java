package com.avvsion.service.rest;

import com.avvsion.service.model.Orders;
import com.avvsion.service.model.Payments;
import com.avvsion.service.model.Response;
import com.avvsion.service.service.OrderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/order", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/create_order")
    public String payment(@RequestParam String amount) throws RazorpayException {
        int amt;
        try{
            amt = Integer.parseInt(amount);
        }
        catch (NumberFormatException e){
            return "Please Enter Number in Form of String";
        }
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_PuP7Zb9GOv2EPt","8jCoZn5ciFu5HIVfaHDBvCyr");
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("amount",amt*100);
        jsonObject.put("currency","INR");
        jsonObject.put("receipt","txn_235425");

        Order order = razorpayClient.orders.create(jsonObject);
        return order.toString();
    }

    @PostMapping("/bookService")
    public ResponseEntity<Response> placeOrder(@Valid @RequestBody Payments payment){

        orderService.placeOrder(payment);

        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Order Placed Successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isOrderPlaces", "true")
                .body(response);
    }

    @GetMapping("/getOrderByServiceId")
    public List<Orders> getOrderByServiceId(@RequestParam int service_id){
        return orderService.getOrdersByServiceId(service_id);
    }
}
