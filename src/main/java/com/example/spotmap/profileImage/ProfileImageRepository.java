package com.example.spotmap.profileImage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Integer> {

    Optional<ProfileImage> findByUsername(String username);
}
