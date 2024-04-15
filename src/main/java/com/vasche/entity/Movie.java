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
    private Integer id;
    private String title;
    private String description;
    private Integer durationMin;
    private Integer minimumAge;
    private String imageUrl;
    private Genre genre;
}
