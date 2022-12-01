package com.example.spotmap;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Objects;

@SpringBootApplication
public class SpotMapApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpotMapApplication.class);
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final URI ROLE_INIT_URL = URI.create("http://localhost:8080/roles/init");
    private static final String ROLE_KEY = "a1f53033-b522-4343-b1b9-db8064142bf3";

    public static void main(String[] args) {
        SpringApplication.run(SpotMapApplication.class, args);
        initRolesFromDatabase();
    }

    private static void initRolesFromDatabase() {
        HttpRequest request = HttpRequest.newBuilder(ROLE_INIT_URL)
                .POST(HttpRequest.BodyPublishers.ofString(ROLE_KEY))
                .build();

        LOGGER.info("Building request for role endpoint...");
        CLIENT.sendAsync(request, responseInfo -> {

            if (responseInfo.statusCode() == 200) {
                LOGGER.info("Successful authenticated with key!");
                LOGGER.info("LOADED ALL KEYS AND TOKENS FROM DATABASE INTO LOCAL STORAGE!");
            } else {
                LOGGER.error("Failed with statuscode " + responseInfo.statusCode());
                LOGGER.error("Please be aware that the app is not running properly!");
            }
            return null;
        });


    }

}
