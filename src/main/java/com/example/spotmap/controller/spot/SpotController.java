package com.example.spotmap.controller.spot;

import com.example.spotmap.rating.Rating;
import com.example.spotmap.spot.Spot;
import com.example.spotmap.spot.SpotRepository;
import com.example.spotmap.spot.SpotType;
import com.example.spotmap.user.User;
import com.example.spotmap.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Spot>> getAllSpots(@RequestParam("token") String token,
                                                  @RequestParam(value = "city", defaultValue = "all") String city,
                                                  @RequestParam(value = "type", defaultValue = "all") SpotType spotType) {
        Optional<User> user = userRepository.findByToken(token);
        List<Spot> spotList = new ArrayList<>();
        List<Spot> spotFilteredList = spotRepository.findAll()
                .stream()
                .filter(s -> city.equals("all") || s.getCity().equals(city))
                .filter(s -> spotType == SpotType.all || s.getSpotType().equals(spotType))
                .toList();

        spotFilteredList.forEach(spot -> {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.getRatingList()) {
                    if (rating.getUser().getUsername().equals(user.get().getUsername())) userRating = rating.getStars();
                }
            }
            spot.setUserRating(userRating);
            spotList.add(spot);
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
    public ResponseEntity<Spot> getSpot(@PathVariable("id") int id, @RequestParam("token") String token) {
        Optional<User> user = userRepository.findByToken(token);
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.get().getRatingList()) {
                    if (rating.getUser().getUsername().equals(user.get().getUsername())) userRating = rating.getStars();
                }
            }
            spot.get().setUserRating(userRating);
            return ResponseEntity.status(HttpStatus.OK).body(spot.get());
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