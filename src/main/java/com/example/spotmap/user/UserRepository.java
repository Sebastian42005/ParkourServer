package com.example.spotmap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where username = :username and password = :password", nativeQuery = true)
    User login(@Param("username") String username, @Param("password") String password);

    @Query(value = "select * from users where token = :token", nativeQuery = true)
    Optional<User> findByToken(@Param("token") String token);

    Optional<User> findByUsername(String username);
}
