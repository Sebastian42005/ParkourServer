package com.example.spotmap.data.rating;

import com.example.spotmap.data.spot.Spot;
import com.example.spotmap.data.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotNull
    @Max(5)
    int stars;
    String comment;

    @JsonBackReference("1")
    @ManyToOne
    Spot spot;

    @JsonBackReference("2")
    @ManyToOne
    User user;
}
