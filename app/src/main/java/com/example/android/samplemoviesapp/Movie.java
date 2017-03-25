package com.example.android.samplemoviesapp;

/**
 * Created by dell on 11/03/2017.
 */

public class Movie {

    private String poster_path;
    private String original_title;
    private String plot_synopsis;
    private int user_rating;
    private String release_date;

    public Movie(){
        // Default Constructor
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public int getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(int user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "poster_path='" + poster_path + '\'' +
                ", original_title='" + original_title + '\'' +
                ", plot_synopsis='" + plot_synopsis + '\'' +
                ", user_rating=" + user_rating +
                ", release_date='" + release_date + '\'' +
                '}';
    }
}
