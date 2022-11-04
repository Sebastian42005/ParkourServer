package com.example.spotmap.controller.spot;

import com.example.spotmap.spot.Spot;
import com.example.spotmap.spot.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    SpotRepository spotRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Spot>> getAllSpots() {
        List<Spot> spotList = new ArrayList<>();
        spotRepository.findAll().forEach(spot -> {
            spot.getUser().setSpotList(null);
            spotList.add(spot);
        });
        return ResponseEntity.status(HttpStatus.OK).body(spotList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpot(@PathVariable("id") int id) {
        Spot spot = spotRepository.findById(id).get();
        spot.getUser().getSpotList().clear();
        return ResponseEntity.status(HttpStatus.OK).body(spot);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSpot(@PathVariable("id") int id) {
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            spotRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted spot");
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Spot not found");
    }
}