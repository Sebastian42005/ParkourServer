package com.example.spotmap.data.spot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum SpotType {
    parkour("parkour"),
    freerunning("freerunning"),
    calisthenics("calisthenics");

    private String value;

    SpotType(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public SpotType fromValue() {
        for (SpotType spotType : SpotType.values()) {
            if (spotType.value.equals(value.toLowerCase(Locale.ROOT).trim())) return spotType;
        }
        throw new IllegalArgumentException("'" + value + "' is not a spot type");
    }
}

