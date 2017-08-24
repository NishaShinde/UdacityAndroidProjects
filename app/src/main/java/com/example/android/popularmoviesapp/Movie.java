/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private long _id;
    private String mPoster;
    private String mBackdropPath;
    private String mTitle;
    private String mOverview;
    private float mRating;
    private String mReleaseDate;

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
        _id = in.readLong();
        mPoster=in.readString();
        mBackdropPath=in.readString();
        mTitle=in.readString();
        mOverview=in.readString();
        mRating=in.readFloat();
        mReleaseDate=in.readString();
    }

    public Movie(){
        // Default Constructor
    }

    public Movie(long _id){
        this._id = _id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(mPoster);
        dest.writeString(mBackdropPath);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeFloat(mRating);
        dest.writeString(mReleaseDate);
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float mRating) {
        this.mRating = mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public long get_id(){
        return _id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "_id=" + _id +
                ", mPoster='" + mPoster + '\'' +
                ", mBackdropPath='" + mBackdropPath + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mRating=" + mRating +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                '}';
    }

    public String getFormattedTitle(){
        StringBuilder title = new StringBuilder(mTitle);
        String[] rDate = mReleaseDate.split("-");
        return title.append(" (").append(rDate[0]).append(")").toString();
    }
}
