package com.vasche.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN,
    CLIENT;

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(role))
                .findFirst();
    }
}
