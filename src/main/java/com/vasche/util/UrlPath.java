package com.vasche.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class UrlPath {
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REGISTRATION = "/registration";
    public static final String PROFILE = "/profile";
    public static final String ADMIN_PANEL = "/admin-panel";
    public static final String MOVIE = "/movie";
    public static final String MOVIES = "/movies";
    public static final String ADD_MOVIE = "/add-movie";
    public static final String EDIT_MOVIE = "/edit-movie";
    public static final String REMOVE_MOVIE = "/remove-movie";
    public static final String IMAGES = "/images";
    public static final String SCREENING = "/screening";
    public static final String SCREENINGS = "/screenings";
    public static final String ADD_SCREENING = "/add-screening";
    public static final String EDIT_SCREENING = "/edit-screening";
    public static final String REMOVE_SCREENING = "/remove-screening";
    public static final String CHOOSE_MOVIE = "/choose-movie";
    public static final String CHOOSE_HALL = "/choose-hall";
    public static final String BOOKMAKER = "/bookmaker";
    public static final String BOOKMAKERS = "/bookmakers";
    public static final String REMOVE_BOOKMAKER = "/remove_bookmaker";
    public static final String HALLS = "/halls";
    public static final String DISPLAYED_MOVIE = "/displayed-movie";
    public static final String DISPLAYED_MOVIES = "/displayed-movies";
    public static final String CHOOSE_SEAT = "/choose-seat";
    public static final String RESERVATION = "/reservation";
    public static final String RESERVATIONS = "/reservations";
    public static final String DOWNLOAD_PDF = "/download-pdf";

    public static String transformUrl(String url) {
        Pattern pattern = Pattern.compile("(\\?movieId=|\\?hallId=|&movieId=|&hallId=).*");

        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            url = url.substring(0, matcher.start());
        }

        int index = url.lastIndexOf('?');
        if (index == -1) {
            url = url + "?";
        } else if (index != url.length() - 1) {
            url += "&";
        }

        return url;
    }
}
