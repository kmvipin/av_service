package com.avvsion.service.annotation;

import com.avvsion.service.validations.CheckStrongPassword;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckStrongPassword.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "password must contain atleast a special character, a lowecase and a uppercase character, a digit";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
