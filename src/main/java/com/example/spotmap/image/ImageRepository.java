package com.example.spotmap.image;

import com.example.spotmap.spot.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
