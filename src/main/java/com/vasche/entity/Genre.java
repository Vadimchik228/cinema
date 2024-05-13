package com.vasche.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Genre {
    DRAMA,
    COMEDY,
    ACTION,
    WESTERN,
    DETECTIVE,
    HISTORICAL,
    ROMANCE,
    ADVENTURE,
    THRILLER,
    HORROR,
    FICTION,
    ALL;


    public static Optional<Genre> find(String genre) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(genre))
                .findFirst();
    }
}
