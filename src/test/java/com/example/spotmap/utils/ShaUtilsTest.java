package com.example.spotmap.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShaUtilsTest {

    @Test
    public void decode() {
        String password = "test123456!";

        String hashed = ShaUtils.decode(password);

        System.out.println(hashed);
        assertNotEquals(password, hashed);
    }

}