package com.example.spotmap.controller.user;

import com.example.rolemanagement.role.Role;
import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.global.Global;
import com.example.spotmap.image.Image;
import com.example.spotmap.image.ImageRepository;
import com.example.spotmap.profileImage.ProfileImage;
import com.example.spotmap.profileImage.ProfileImageRepository;
import com.example.spotmap.spot.Spot;
import com.example.spotmap.spot.SpotRepository;
import com.example.spotmap.spot.SpotResponse;
import com.example.spotmap.user.User;
import com.example.spotmap.user.UserRepository;
import com.example.spotmap.utils.ShaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Validated
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SpotRepository spotRepository;

    @Autowired
    ProfileImageRepository profileImageRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setRole(Role.USER);
        user.setPassword(ShaUtils.decode(user.getPassword()));
        AtomicBoolean userExists = new AtomicBoolean(false);
        userRepository.findAll().forEach(eachUser -> {
            if (eachUser.getUsername().equals(user.getUsername())) {
                userExists.set(true);
            }
        });
        if (!userExists.get()) {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
        }else return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }

    @RequiredRole(Role.USER)
    @PostMapping("/post-spot")
    public ResponseEntity<SpotResponse> createSpot(@RequestBody Spot spot, @RequestParam String token) {
        System.out.println("Drinnen");
        Optional<User> user = userRepository.findByToken(token);
        if(user.isPresent()) {
            spot.setUser(user.get());
            user.get().getSpotList().add(spot);
            Spot savedSpot = spotRepository.save(spot);
            System.out.println("Saved");
            return ResponseEntity.status(HttpStatus.OK).body(SpotResponse.getResponseFromSpot(savedSpot, 0));
        }else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            user.setToken("");
            userList.add(user);
        });
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @Data
    @AllArgsConstructor
    class BasicResponse {
        String path;
    }

    @PostMapping("/profile")
    public ResponseEntity<BasicResponse> setProfilePic(@RequestParam("token") String token, @RequestParam("image") MultipartFile image) throws IOException {
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            profileImageRepository.save(new ProfileImage(user.get().getUsername(), image.getContentType(), image.getBytes()));
            String path = Global.INSTANCE.getUrl() + "/user/profile/" + user.get().getUsername();
            return ResponseEntity.status(HttpStatus.OK).body(new BasicResponse(path));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("profile/{username}")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable("username") String username) {
        Optional<ProfileImage> image = profileImageRepository.findByUsername(username);
        if (image.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(image.get().getType()))
                    .body(image.get().getBytes());
        }else {
            ProfileImage administrator = profileImageRepository.findByUsername("administrator").get();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.valueOf(administrator.getType()))
                    .body(administrator.getBytes());
        }
    }

    @RequiredRole(Role.USER)
    @GetMapping()
    public ResponseEntity<User> getUser(@RequestParam("token") String token) {
        Optional<User> user = userRepository.findByToken(token);

        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}