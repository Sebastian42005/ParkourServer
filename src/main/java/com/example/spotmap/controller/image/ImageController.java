package com.example.spotmap.controller.image;

import com.example.spotmap.data.image.Image;
import com.example.spotmap.data.image.ImageRepository;
import com.example.spotmap.data.spot.Spot;
import com.example.spotmap.data.spot.SpotRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    SpotRepository spotRepository;


    @Data
    @AllArgsConstructor
    class Response {
        int id;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Response> setSpotImage(@PathVariable("id") int id, @RequestParam("image") MultipartFile image) throws IOException {
        Optional<Spot> spot = spotRepository.findById(id);
        if (spot.isPresent()) {
            Image picture = new Image(id, image.getContentType(), image.getBytes());
            imageRepository.save(picture);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(picture.getId()));
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") int id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(image.get().getType()))
                    .body(image.get().getBytes());
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
