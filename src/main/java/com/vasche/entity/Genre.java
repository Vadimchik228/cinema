package com.vasche.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Genre {
    DRAMA,
    COMEDY;

    public static Optional<Genre> find(String genre) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(genre))
                .findFirst();
    }
}
