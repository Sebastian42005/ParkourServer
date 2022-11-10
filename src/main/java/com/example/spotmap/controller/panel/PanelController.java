package com.example.spotmap.controller.panel;

import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import com.example.spotmap.utils.ShaUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/panel")
public class PanelController {

    @Autowired
    private UserRepository userRepository;

    @AllArgsConstructor
    static class LoginCredentials {
        String username;
        String password;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginCredentials loginCredentials) {
        Optional<User> user = userRepository.login(loginCredentials.username, ShaUtils.decode(loginCredentials.password));

        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
        }

        User present = user.get();

        if(present.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have to be an admin at least!");
        }

        return present;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

}
