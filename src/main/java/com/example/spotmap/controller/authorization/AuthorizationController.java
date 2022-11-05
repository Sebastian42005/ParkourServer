package com.example.spotmap.controller.authorization;

import com.example.spotmap.role.management.RoleHandler;
import com.example.spotmap.user.User;
import com.example.spotmap.user.UserRepository;
import com.example.spotmap.utils.ShaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    @AllArgsConstructor
    static class LoginCredentials {
        String username;
        String password;
    }

    @Data
    @NoArgsConstructor
    static class LoginResponse {
        String token;
    }

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    private LoginResponse login(@RequestBody LoginCredentials loginCredentials) {
        User user = userRepository.login(loginCredentials.username, ShaUtils.decode(loginCredentials.password));
        LoginResponse loginResponse = new LoginResponse();
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setTokenExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
            userRepository.save(user);
            RoleHandler.push(token, user.getRole());
            loginResponse.token = token;
        }
        return loginResponse;
    }
}