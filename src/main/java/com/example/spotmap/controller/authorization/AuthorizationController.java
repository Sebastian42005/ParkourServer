package com.example.spotmap.controller.authorization;

import com.example.spotmap.role.management.RoleHandler;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
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
import java.util.Optional;
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
    public LoginResponse login(@RequestBody LoginCredentials loginCredentials) {
        Optional<User> user = userRepository.login(loginCredentials.username, ShaUtils.decode(loginCredentials.password));
        LoginResponse loginResponse = new LoginResponse();
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setToken(token);
            user.get().setTokenExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
            userRepository.save(user.get());
            RoleHandler.push(token, user.get().getRole());
            loginResponse.token = token;
        }
        return loginResponse;
    }
}
