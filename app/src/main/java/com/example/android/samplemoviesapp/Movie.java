package com.example.android.samplemoviesapp;

/**
 * Created by dell on 11/03/2017.
 */

public class Movie {

    private String posterPath;

    Movie(String poster){
        posterPath = poster;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
