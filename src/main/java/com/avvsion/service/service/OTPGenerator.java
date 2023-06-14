package com.avvsion.service.service;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
public class OTPGenerator {
    private static final int OTP_LENGTH = 6;

    public String generateOTP() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .build();

        return generator.generate(OTP_LENGTH);
    }
}
