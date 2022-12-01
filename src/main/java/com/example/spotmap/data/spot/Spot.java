package com.example.spotmap.data.spot;

import com.example.spotmap.data.company.Company;
import com.example.spotmap.data.rating.Rating;
import com.example.spotmap.data.user.User;
import com.example.spotmap.role.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "spots")
public class Spot{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    double longitude;
    @NotNull
    double latitude;

    String city;

    String description;

    SpotType spotType;

    int userRating;
    double rating;

    @JsonBackReference("3")
    @ManyToOne
    User user;

    @JsonManagedReference("1")
    @OneToMany(mappedBy = "spot")
    List<Rating> ratingList = new ArrayList<>();

    public SpotResponseBasic getBasicResponse() {
        return new SpotResponseBasic(this);
    }

    @Data
    @NoArgsConstructor
    public class SpotResponseBasic {
        int id;
        double longitude;
        double latitude;
        String city;
        String description;
        SpotType spotType;
        int userRating;
        double rating;
        SpotUserResponseBasic user;

        public SpotResponseBasic(Spot spot) {
            this.id = spot.id;
            this.longitude = spot.longitude;
            this.latitude = spot.latitude;
            this.city = spot.city;
            this.description = spot.description;
            this.spotType = spot.spotType;
            this.userRating = spot.userRating;
            this.rating = spot.rating;
            this.user = new SpotUserResponseBasic(spot.user);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SpotUserResponseBasic {
        int id;
        String username;
        Role role;
        Company company;

        public SpotUserResponseBasic(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.role = user.getRole();
            this.company = user.getCompany();
        }
    }
}
