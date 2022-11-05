package com.example.spotmap.annotations;

import com.example.spotmap.role.management.RequiredRoleValidation;
import com.example.spotmap.role.role.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Validated
@Constraint(validatedBy = {RequiredRoleValidation.class})
@Target({ METHOD, FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ReportAsSingleViolation
public @interface RequiredRole {

    Role value();

    String message() default
            "That role does not meet the requirements";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}