package com.example.spotmap.role;

import com.example.spotmap.role.management.RoleHandler;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
class RoleRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRest.class);
    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @PostMapping("/roles/init")
    public ResponseEntity<String> initRoles(@RequestBody String key) {
        File file = new File("src/main/java/com/example/spotmap/role/key");
        String content = Files.readString(Path.of(file.getPath())).trim();

        if(!key.equals(content)) {
            LOGGER.error("Role init wrong key provided {}", key);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Wrong key");
        }

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            RoleHandler.push(user.getToken(), user.getRole());
        }

        return ResponseEntity.status(HttpStatus.OK).body("LOADED " + userList.size() + " ROLES AND TOKEN INTO LOCAL STORAGE");
    }
}
