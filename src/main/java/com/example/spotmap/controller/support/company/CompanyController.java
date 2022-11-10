package com.example.spotmap.controller.support.company;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.company.Company;
import com.example.spotmap.data.company.CompanyRepository;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/company")
public class CompanyController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @AllArgsConstructor
    @Data
    class RequestPermissionResponseClass {
        String message;
    }

    @RequiredRole(Role.USER)
    @PostMapping("/request-permission")
    public ResponseEntity<RequestPermissionResponseClass> requestPermission(@RequestParam("token") String token, @RequestParam("message") String message) {
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            if (user.get().getRole() == Role.USER) {
                Company company = new Company();
                company.setMessage(message);
                company.setUsername(user.get().getUsername());
                companyRepository.save(company);
                return ResponseEntity.ok().body(new RequestPermissionResponseClass("sent request"));
            }else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
