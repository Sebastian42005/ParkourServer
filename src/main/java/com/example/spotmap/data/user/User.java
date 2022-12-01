package com.example.spotmap.data.user;

import com.example.spotmap.data.company.Company;
import com.example.spotmap.data.spot.SpotType;
import com.example.spotmap.role.role.Role;
import com.example.spotmap.data.rating.Rating;
import com.example.spotmap.data.spot.Spot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String username;

    @NotNull
    String password;

    Role role;

    String token;

    Instant tokenExpiresAt = null;

    @JsonManagedReference("3")
    @OneToMany(mappedBy = "user")
    List<Spot> spotList = new ArrayList<>();

    @JsonManagedReference("2")
    @OneToMany(mappedBy = "user")
    List<Rating> ratingList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    Company company;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_companys",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") })
    @JsonIgnore
    List<Company> companyList = new ArrayList<>();

    public UserResponseBasic getBasicResponse() {
        return new UserResponseBasic(this);
    }

    @Data
    @NoArgsConstructor
    public class UserResponseBasic {
        int id;
        String username;
        Role role;
        Company company;
        List<UserSpotResponseBasic> spotList;

        public UserResponseBasic(User user) {
            this.id = user.id;
            this.username = user.username;
            this.role = user.role;
            this.company = user.company;
            this.spotList = user.spotList.stream().map(UserSpotResponseBasic::new).collect(Collectors.toList());
        }
    }

    @Data
    @NoArgsConstructor
    public static class UserSpotResponseBasic {
        int id;
        double longitude;
        double latitude;
        String city;
        String description;
        SpotType spotType;
        int userRating;
        double rating;

        public UserSpotResponseBasic(Spot spot) {
            this.id = spot.getId();
            this.longitude = spot.getLongitude();
            this.latitude = spot.getLatitude();
            this.city = spot.getCity();
            this.description = spot.getDescription();
            this.spotType = spot.getSpotType();
            this.userRating = spot.getUserRating();
            this.rating = spot.getRating();
        }
    }
}
