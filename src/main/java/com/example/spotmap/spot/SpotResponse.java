package com.example.spotmap.spot;

import com.example.spotmap.rating.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SpotResponse {
    int id;
    double longitude;
    double latitude;
    String city;
    String description;
    SpotType spotType;
    double rating;
    int userRating;

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

    public static SpotResponse getResponseFromSpot(Spot spot, int userRating) {
        return new SpotResponse(
                spot.getId(),
                spot.getLongitude(),
                spot.getLatitude(),
                spot.getCity(),
                spot.getDescription(),
                spot.getSpotType(),
                getAverageRating(spot.getRatingList()),
                userRating);
    }
}
