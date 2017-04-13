package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 11/03/2017.
 */

public class Movie implements Parcelable {

    private String poster_path;
    private String original_title;
    private String plot_synopsis;
    private int user_rating;
    private String release_date;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    private Movie(Parcel in){
        poster_path=in.readString();
        original_title=in.readString();
        plot_synopsis=in.readString();
        user_rating=in.readInt();
        release_date=in.readString();
    }

    public Movie(){
        // Default Constructor
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(plot_synopsis);
        dest.writeInt(user_rating);
        dest.writeString(release_date);
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
