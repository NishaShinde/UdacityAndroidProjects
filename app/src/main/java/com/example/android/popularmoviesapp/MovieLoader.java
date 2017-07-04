package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;

import java.util.List;

/**
 * Created by dell on 26/03/2017.
 */

class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String TAG = MovieLoader.class.getSimpleName();
    private String mUrl;


    public MovieLoader(Context context,String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        return NetworkUtils.fetchPopularMovies(mUrl);
    }
}
