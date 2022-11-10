package com.example.spotmap.controller.token;

import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/token")
@Validated
public class TokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    UserRepository userRepository;

    @Data
    @NoArgsConstructor
    class Response {
        boolean verified = false;
    }

    @GetMapping("/verify")
    public ResponseEntity<Response> isTokenValid(@RequestParam("token") String token) {
        final Optional<User> targetUser = userRepository.findByToken(token);
        Response response = new Response();

        if(targetUser.isEmpty()) {
            LOGGER.error("No user found with id {}", token);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        targetUser.get().setToken(token);

        response.verified = Instant.now().isBefore(targetUser.get().getTokenExpiresAt());

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
