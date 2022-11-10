package com.example.spotmap.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where username = :username and password = :password", nativeQuery = true)
    Optional<User> login(@Param("username") String username, @Param("password") String password);

    @Query(value = "select * from users where token = :token", nativeQuery = true)
    Optional<User> findByToken(@Param("token") String token);

    Optional<User> findByUsername(String username);
}
