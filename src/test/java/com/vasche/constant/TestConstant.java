package com.vasche.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public final static String CLEAN_MOVIES_SQL = "DELETE FROM movies;";
    public final static String CLEAN_SEATS_SQL = "DELETE FROM seats;";
    public final static String CLEAN_HALLS_SQL = "DELETE FROM halls;";
    public final static String CLEAN_LINES_SQL = "DELETE FROM lines;";
    public final static String CLEAN_SCREENINGS_SQL = "DELETE FROM screenings;";
    public final static String CLEAN_RESERVATIONS_SQL = "DELETE FROM reservations;";
    public final static String CLEAN_USERS_SQL = "DELETE FROM users;";

    public static final String HALL_NAME1 = "Hall 1";
    public static final String HALL_NAME2 = "Hall 2";
    public static final String HALL_NAME3 = "Hall 3";
    public static final String MOVIE_TITLE1 = "1 + 1";
    public static final String MOVIE_TITLE2 = "Spider man";
    public static final String MOVIE_TITLE3 = "It";
    public static final String EMAIL1 = "schebetovskiy@gmail.com";
    public static final String EMAIL2 = "vadimschebetovskiy@gmail.com";
    public static final String EMAIL3 = "schebetovskiyvadim@gmail.com";
}
