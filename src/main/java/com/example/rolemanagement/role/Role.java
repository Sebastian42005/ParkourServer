package com.example.rolemanagement.role;

import lombok.Getter;

public enum Role {
    GUEST(0),
    USER(1),
    ADMIN(2);

    @Getter
    private final int weight;
    Role(int weight) {
        this.weight = weight;
    }

}
