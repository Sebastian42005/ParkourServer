package com.example.spotmap.controller.company;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.company.Company;
import com.example.spotmap.data.company.CompanyRepository;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @RequiredRole(Role.COMPANY)
    @PutMapping("edit")
    public ResponseEntity<Company> setUpCompany(@RequestParam String token, @RequestBody Company company) {
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            if (companyRepository.findById(company.getId()).isPresent()) {
                if (user.get().getCompany().getId() == company.getId()) {
                    return ResponseEntity.ok().body(companyRepository.save(company));
                }else return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RequiredRole(Role.COMPANY)
    @PutMapping("/{id}/add-member/{username}")
    public ResponseEntity<Company> addMemberToCompany(@RequestParam String token, @PathVariable String username, @PathVariable int id) {
        Optional<User> user = userRepository.findByToken(token);
        Optional<User> usernameUser = userRepository.findByUsername(username);
        Optional<Company> company = companyRepository.findById(id);
        if (user.isPresent()) {
            if (company.isPresent()) {
                if (user.get().getCompany().getId() == company.get().getId()) {
                    if (usernameUser.isPresent()) {
                        usernameUser.get().getCompanyList().add(company.get());
                        userRepository.save(user.get());

                        return ResponseEntity.ok().body(companyRepository.save(company.get()));
                    }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                } else return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<User>> getCompanyMembers(@PathVariable int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok().body(company.get().getUserList());
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
