package com.example.spotmap.data.spot;

import com.example.spotmap.data.rating.Rating;
import com.example.spotmap.data.user.User;
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
}
