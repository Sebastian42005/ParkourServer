package com.example.spotmap.controller.rating;

import com.example.spotmap.annotations.RequiredRole;
import com.example.spotmap.data.rating.Rating;
import com.example.spotmap.data.rating.RatingRepository;
import com.example.spotmap.data.spot.Spot;
import com.example.spotmap.data.spot.SpotRepository;
import com.example.spotmap.data.user.User;
import com.example.spotmap.data.user.UserRepository;
import com.example.spotmap.role.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @RequiredRole(Role.USER)
    @PostMapping("/spot")
    public ResponseEntity<Rating> rateSpot(@RequestParam("token") String token, @RequestParam("spot") int spotId, @RequestBody Rating rating) {
        Optional<User> user = userRepository.findByToken(token);
        Optional<Spot> spot = spotRepository.findById(spotId);
        if (user.isPresent()) {
            if (spot.isPresent()) {
                rating.setUser(user.get());
                rating.setSpot(spot.get());
                boolean rated = false;
                for (Rating currentRating : spot.get().getRatingList()) {
                    if (currentRating.getUser().getUsername().equals(user.get().getUsername())) {
                        rating.setId(currentRating.getId());
                        rated = true;
                    }
                }
                if (!rated) {
                    spot.get().getRatingList().add(rating);
                }
                ratingRepository.save(rating);
                double averageRating = getAverageRating(spot.get().getRatingList());
                spot.get().setRating(averageRating);
                spotRepository.save(spot.get());

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RatingInfoResponse {
        double rating;
        int userRating;
    }

    @GetMapping("/spot/{id}/info")
    public ResponseEntity<RatingInfoResponse> getSpotRatingInfo(@PathVariable int id, @RequestParam(value = "token", defaultValue = "") String token) {
        Optional<Spot> spot = spotRepository.findById(id);
        Optional<User> user = userRepository.findByToken(token);
        if (spot.isPresent()) {
            int userRating = 0;
            if (user.isPresent()) {
                for (Rating rating : spot.get().getRatingList()) {
                    if (rating.getUser().getUsername().equals(user.get().getUsername())) userRating = rating.getStars();
                }
            }
            spot.get().setUserRating(userRating);
            return ResponseEntity.status(HttpStatus.OK).body(new RatingInfoResponse(getAverageRating(spot.get().getRatingList()), spot.get().getUserRating()));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private static double getAverageRating(List<Rating> ratingList) {
        if (ratingList.isEmpty()) return 0;
        int sum = 0;
        for (Rating currentRating : ratingList) {
            sum += currentRating.getStars();
        }
        return round((double) sum / ratingList.size(), 2);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
