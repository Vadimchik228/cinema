package com.vasche.dto.filter;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class ScreeningFilterDto {
    private final String title;
    private final String genre;
    private final String date;
}
