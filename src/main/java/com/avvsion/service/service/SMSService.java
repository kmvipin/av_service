package com.avvsion.service.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendOTP(String phoneNumber, String otp) {
        Twilio.init(accountSid, authToken);

        String messageBody = "Your OTP is: " + otp;

        Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber), messageBody)
                .create();

        System.out.println("SMS sent with SID: " + message.getSid());
    }
}
