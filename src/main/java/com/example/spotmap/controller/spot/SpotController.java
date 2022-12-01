package com.example.spotmap.controller.spot;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.rating.Rating;
import com.example.spotmap.data.spot.Spot;
import com.example.spotmap.data.spot.SpotRepository;
import com.example.spotmap.data.spot.SpotType;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Spot.SpotResponseBasic>> getAllSpots(@RequestParam(value = "token", defaultValue = "") String token,
                                                                    @RequestParam(value = "city", defaultValue = "all") String city,
                                                                    @RequestParam(value = "types") Optional<List<SpotType>> spotTypesParam) {
        Optional<User> user = userRepository.findByToken(token);
        List<Spot> spotList = new ArrayList<>();
        List<SpotType> spotTypeList = new ArrayList<>();
        spotTypesParam.ifPresent(spotTypeList::addAll);

        List<Spot> spotFilteredList = spotRepository.findAll()
                .stream()
                .filter(s -> city.equals("all") || s.getCity().equals(city))
                .filter(s -> spotTypeList.isEmpty() || spotTypeList.contains(s.getSpotType()))
                .toList();

        spotFilteredList.forEach(spot -> {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.getRatingList()) {
                    if (rating.getUser().getId() == user.get().getId()) userRating = rating.getStars();
                }
            }
            spot.setUserRating(userRating);
            spotList.add(spot);
        });
        return ResponseEntity.status(HttpStatus.OK).body(spotList.stream().map(Spot::getBasicResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spot.SpotResponseBasic> getSpot(@PathVariable("id") int id, @RequestParam("token") String token) {
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
            return ResponseEntity.status(HttpStatus.OK).body(spot.get().getBasicResponse());
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RequiredRole(Role.USER)
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSpot(@PathVariable("id") int id, @RequestParam("token") String token) {
        Optional<User> user = userRepository.findByToken(token);
        if (user.isPresent()) {
            Optional<Spot> spot = spotRepository.findById(id);
            if (spot.isPresent()) {
                if (spot.get().getUser().getUsername().equals(user.get().getUsername())) {
                    spotRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Deleted spot");
                }else return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("This is not your spot");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Spot not found");
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
