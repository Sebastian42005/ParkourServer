package com.example.spotmap.controller.support.company;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.company.CompanyRequest;
import com.example.spotmap.data.company.CompanyRequestRepository;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/company-requests")
public class CompanyController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRequestRepository companyRequestRepository;

    @AllArgsConstructor
    @Data
    class RequestPermissionResponseClass {
        String message;
    }

    @RequiredRole(Role.USER)
    @PostMapping("/request-permission")
    public ResponseEntity<RequestPermissionResponseClass> requestPermission(@RequestParam("token") String token, @RequestParam("message") String message) {
        List<CompanyRequest> companyRequestList = companyRequestRepository.findAll();
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            if (!companyRequestList.stream().anyMatch(companyRequest -> user.get().getUsername().equals(companyRequest.getUsername()))) {
                if (user.get().getRole() == Role.USER) {
                    CompanyRequest companyRequest = new CompanyRequest();
                    companyRequest.setMessage(message);
                    companyRequest.setUsername(user.get().getUsername());
                    companyRequestRepository.save(companyRequest);
                    return ResponseEntity.ok().body(new RequestPermissionResponseClass("sent request"));
                } else return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
            }else return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RequiredRole(Role.ADMIN)
    @GetMapping("/all")
    public ResponseEntity<List<CompanyRequest>> getCompanyRequests(@RequestParam("token") String token) {
        return ResponseEntity.ok().body(companyRequestRepository.findAll());
    }

    @RequiredRole(Role.ADMIN)
    @PutMapping("/accept-request")
    public ResponseEntity<RequestPermissionResponseClass> acceptRequest(@RequestParam("token") String token, @RequestParam("id") int id) {
        Optional<CompanyRequest> companyRequest = companyRequestRepository.findById(id);
        if (companyRequest.isPresent()) {
            Optional<User> user = userRepository.findByUsername(companyRequest.get().getUsername());
            if (user.isPresent()) {
                user.get().setRole(Role.COMPANY);
                userRepository.save(user.get());
                companyRequestRepository.delete(companyRequest.get());
                return ResponseEntity.ok().body(new RequestPermissionResponseClass("accepted"));
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestPermissionResponseClass("user not found"));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestPermissionResponseClass("company request not found"));
    }

    @RequiredRole(Role.ADMIN)
    @PutMapping("decline-request")
    public ResponseEntity<RequestPermissionResponseClass> declineRequest(@RequestParam("token") String token, @RequestParam("id") int id) {
        Optional<CompanyRequest> companyRequest = companyRequestRepository.findById(id);
        if (companyRequest.isPresent()) {
            companyRequestRepository.delete(companyRequest.get());
            return ResponseEntity.ok().body(new RequestPermissionResponseClass("accepted"));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestPermissionResponseClass("company request not found"));
    }
}
