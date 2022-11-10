package com.example.spotmap.role.role;

import lombok.Getter;

public enum Role {
    GUEST(0),
    USER(1),
    COMPANY(2),
    ADMIN(3);

    @Getter
    private final int weight;
    Role(int weight) {
        this.weight = weight;
    }

}
