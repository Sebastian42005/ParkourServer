package com.example.spotmap.controller.company.timetable;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.company.CompanyRepository;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.file.FileHandler;
import com.example.spotmap.role.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/time-table")
public class TimeTableController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;


    private final FileHandler<TimeTable> fileHandler = FileHandler.getInstance();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class BasicResponse {
        String message;
    }

    @RequiredRole(Role.COMPANY)
    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity<BasicResponse> createTimeTable(@RequestParam String token, @RequestBody TimeTable timeTable) {
        User user = userRepository.findByToken(token).orElseThrow();
        fileHandler.writeFile(timeTable, "Company" + user.getCompany().getId());

        return ResponseEntity.status(HttpStatus.OK).body(new BasicResponse("Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeTable> getTimeTable(@PathVariable int id) {
        TimeTable timeTable = fileHandler.toObject("Company" + id, TimeTable.class);
        if (timeTable == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok().body(timeTable);
    }
}
