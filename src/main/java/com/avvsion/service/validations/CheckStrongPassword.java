package com.avvsion.service.validations;

import com.avvsion.service.annotation.StrongPassword;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CheckStrongPassword implements
        ConstraintValidator<StrongPassword, String> {

    List<Character> special = Arrays.asList('!','@','#','$','%','^','&','*','(',')','-','+','_','.',',');

    boolean specialChar = false;
    boolean upperCase = false;
    boolean lowerCase = false;
    boolean digit = false;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String pass, ConstraintValidatorContext constraintValidatorContext) {
        if(pass == null)
            return false;

        for(int i = 0; i < pass.length(); i++){
            if(specialChar != true && special.contains(pass.charAt(i))){
                specialChar = true;
                continue;
            }
            if(upperCase != true && pass.charAt(i) >= 'A' && pass.charAt(i) <= 'Z'){
                upperCase = true;
                continue;
            }
            if(lowerCase != true && pass.charAt(i) >= 'a' && pass.charAt(i) <= 'z'){
                lowerCase = true;
                continue;
            }
            if(digit != true && pass.charAt(i) >= '0' && pass.charAt(i) <= '9'){
                digit = true;
            }
        }

        return specialChar && upperCase && lowerCase && digit;
    }
}
