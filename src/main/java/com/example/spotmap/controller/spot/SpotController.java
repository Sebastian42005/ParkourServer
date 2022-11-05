package com.example.spotmap.controller.spot;

import com.example.spotmap.rating.Rating;
import com.example.spotmap.spot.Spot;
import com.example.spotmap.spot.SpotRepository;
import com.example.spotmap.spot.SpotResponse;
import com.example.spotmap.spot.SpotType;
import com.example.spotmap.user.User;
import com.example.spotmap.user.UserRepository;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<SpotResponse>> getAllSpots(@RequestParam("token") String token) {
        Optional<User> user = userRepository.findByToken(token);
        List<SpotResponse> spotList = new ArrayList<>();
        spotRepository.findAll().forEach(spot -> {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.getRatingList()) {
                    if (rating.getUser().getUsername().equals(user.get().getUsername())) userRating = rating.getStars();
                }
            }
            spotList.add(SpotResponse.getResponseFromSpot(spot, userRating));
        });
        return ResponseEntity.status(HttpStatus.OK).body(spotList);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<User> getSpotUser(@PathVariable("id") int id) {
        Spot spot = spotRepository.findById(id).get();
        spot.getUser().getSpotList().clear();
        return ResponseEntity.status(HttpStatus.OK).body(spot.getUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotResponse> getSpot(@PathVariable("id") int id, @RequestParam("token") String token) {
        Optional<User> user = userRepository.findByToken(token);
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.get().getRatingList()) {
                    if (rating.getUser().getUsername().equals(user.get().getUsername())) userRating = rating.getStars();
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(SpotResponse.getResponseFromSpot(spot.get(), userRating));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSpot(@PathVariable("id") int id) {
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            spotRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted spot");
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Spot not found");
    }
}