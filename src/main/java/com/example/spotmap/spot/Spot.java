package com.example.spotmap.spot;

import com.example.spotmap.rating.Rating;
import com.example.spotmap.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
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