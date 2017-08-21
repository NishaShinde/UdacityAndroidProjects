/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */
package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,SharedPreferences.OnSharedPreferenceChangeListener,MovieAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MOVIES_LOADER_ID=0;
    private static boolean PREFERENCES_HAVE_BEEN_CHANGED;
    private static final String QUERY_API_KEY = "api_key";

    private MovieAdapter mAdapter;
    private TextView mEmptyTextView;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private static final int NUM_OF_COLUMNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.popularmoviesapp.R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movies);
        mEmptyTextView = (TextView)findViewById(R.id.emptyTextView);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this,NUM_OF_COLUMNS);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this,this);

        mRecyclerView.setAdapter(mAdapter);
        loadMovieData();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PREFERENCES_HAVE_BEEN_CHANGED){
            getLoaderManager().restartLoader(MOVIES_LOADER_ID,null,this);
            PREFERENCES_HAVE_BEEN_CHANGED=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> movieList = new ArrayList<>();

            @Override
            protected void onStartLoading() {
                if(!movieList.isEmpty()){
                    deliverResult(movieList);
                }else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movieList.clear();
                movieList.addAll(data);
                super.deliverResult(data);
            }

            @Override
            public List<Movie> loadInBackground() {
                String uri = buildUri();

                if(uri == null){
                    return null;
                }
                List<Movie> movies = NetworkUtils.fetchPopularMovies(uri);
                Log.d(TAG,"loadInBackground:"+movies.size());
                return movies;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.GONE);

        if(data!=null && !data.isEmpty()){
            mAdapter.setMovieData(data);
            showMovieData();
        }else {
            Log.d(TAG,"Caught in No Movies found."+data+data.size());
            showErrorMessage();
            mEmptyTextView.setText(getString(R.string.no_movies));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setMovieData(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.android.popularmoviesapp.R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        if(itemThatWasClicked == R.id.action_settings){
            Intent intentForSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(intentForSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_CHANGED=true;
    }

    @Override
    public void onItemClick(Movie movie) {
        if(movie == null) return;

        Intent movieIntent = new Intent(MainActivity.this,DetailsActivity.class);
        movieIntent.putExtra(Intent.EXTRA_TEXT,movie);
        startActivity(movieIntent);
        Log.d(TAG,"onItemClick: "+movie.getTitle());
    }

    private String buildUri(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortKey = getString(R.string.sort_by_key);
        String defaultSort = getString(R.string.sort_by_popularity);
        String sortType=sharedPreferences.getString(sortKey,defaultSort);
        Uri builtUri = Uri.parse(getString(R.string.movie_dp_api)).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String uriString = builtUri.toString();
        Log.d(TAG, "buildUri: Uri built is "+uriString);

        return uriString;
    }

    private void loadMovieData() {
        if(isNetworkAvailable()){
            getLoaderManager().initLoader(MOVIES_LOADER_ID,null,this);
        }else{
            showErrorMessage();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mEmptyTextView.setText(getString(R.string.no_internet));
        }
    }


    private void showMovieData(){
        mEmptyTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }



}
