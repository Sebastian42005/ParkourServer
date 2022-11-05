package com.example.spotmap.user;

import com.example.spotmap.role.role.Role;
import com.example.spotmap.rating.Rating;
import com.example.spotmap.spot.Spot;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
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
}
