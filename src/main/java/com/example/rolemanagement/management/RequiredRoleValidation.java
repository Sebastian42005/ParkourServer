package com.example.rolemanagement.management;

import com.example.rolemanagement.role.Role;
import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class RequiredRoleValidation implements ConstraintValidator<RequiredRole, Object[]> {

    private static Logger LOGGER = LoggerFactory.getLogger(RequiredRoleValidation.class);

    private Role role;
    private String token;


    @Override
    public void initialize(RequiredRole constraintAnnotation) {
        this.role = constraintAnnotation.value();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if(!this.isValidToken(value) && role != Role.GUEST) {
            LOGGER.error("No token provided or no valid token provided!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No token provided or no valid token provided!");
        }

        Role tokensRole = this.getTokensRole();

        if(tokensRole == Role.GUEST) {
            return true;
        }

        if(tokensRole.getWeight() < role.getWeight()) {
            LOGGER.error("No permissions to access this function!");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permissions to access this function!");
        }

        return true;
    }

    private Role getTokensRole() {
        return RoleHandler.get(token) == null ? Role.GUEST : RoleHandler.get(token);
    }

    private boolean isValidToken(Object[] parameters) {
        for(Object cur : parameters) {
            if(cur instanceof String) {
                if(RoleHandler.storage.containsKey(cur.toString())) {
                    this.token = cur.toString();
                    return true;
                }
            }
        }
        return false;
    }
}
