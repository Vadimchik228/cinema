package com.vasche.dto.filter;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ScreeningFilterDto {
    String title;
    Genre genre;
    LocalDate date;
}
