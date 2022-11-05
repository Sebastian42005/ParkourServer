package com.example.spotmap.controller.rating;

import com.example.spotmap.rating.Rating;
import com.example.spotmap.rating.RatingRepository;
import com.example.spotmap.spot.Spot;
import com.example.spotmap.spot.SpotRepository;
import com.example.spotmap.user.User;
import com.example.spotmap.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/rating")
public class RatingController {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpotRepository spotRepository;

    @PostMapping("/spot")
    public ResponseEntity<Rating> rateSpot(@RequestParam("token") String token, @RequestParam("spot") int spotId, @RequestBody Rating rating) {
        Optional<User> user = userRepository.findByToken(token);
        Optional<Spot> spot = spotRepository.findById(spotId);
        if (user.isPresent()) {
            if (spot.isPresent()) {
                rating.setUser(user.get());
                rating.setSpot(spot.get());
                for (Rating currentRating : spot.get().getRatingList()) {
                    if (currentRating.getUser().getUsername().equals(user.get().getUsername())) rating.setId(currentRating.getId());
                }
                ratingRepository.save(rating);

                return ResponseEntity.status(HttpStatus.OK).body(rating);
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/spot/{id}")
    public ResponseEntity<List<Rating>> getSpotRatings(@PathVariable("id") int id) {
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(spot.get().getRatingList());
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}
