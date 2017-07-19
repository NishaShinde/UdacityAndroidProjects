/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;

import java.util.List;


class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String TAG = MovieLoader.class.getSimpleName();
    private String mUrl;
    private List<Movie> movieList;


    public MovieLoader(Context context,String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if(movieList != null){
            deliverResult(movieList);
        }else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movieList=data;
        super.deliverResult(data);
    }

    @Override
    public List<Movie> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        return NetworkUtils.fetchPopularMovies(mUrl);
    }
}
