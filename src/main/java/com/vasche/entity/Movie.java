package com.vasche.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    private int id;
    private String title;
    private String description;
    private int durationMin;
    private int minimumAge;
    private String imageUrl;
    private Genre genre;
}
