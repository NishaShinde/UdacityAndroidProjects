/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MOVIES_LOADER_ID=0;

    private static boolean PREFERENCES_HAVE_BEEN_CHANGED=false;

    private static final String MOVIE_DB_API = "https://api.themoviedb.org/3/movie";
    private static final String QUERY_API_KEY = "api_key";

    private MovieAdapter mAdapter;
    private TextView mEmptyTextView;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.popularmoviesapp.R.layout.activity_main);

        mEmptyTextView = (TextView)findViewById(com.example.android.popularmoviesapp.R.id.emptyTextView);

        mAdapter = new MovieAdapter(this,new ArrayList<Movie>());

        mGridView = (GridView) findViewById(com.example.android.popularmoviesapp.R.id.movie_grid);
        mGridView.setEmptyView(mEmptyTextView);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie currentMovie = mAdapter.getItem(position);
                Intent movieIntent = new Intent(getApplicationContext(),DetailsActivity.class);
                movieIntent.putExtra(Intent.EXTRA_TEXT,currentMovie);
                startActivity(movieIntent);
                Log.d(TAG, "Clicked movie: "+currentMovie.toString());
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            getLoaderManager().initLoader(MOVIES_LOADER_ID,null,this);
        }else{
            dismissLoadingIndicator();
            mEmptyTextView.setText(getString(R.string.no_internet));
        }
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

    private String buildUri(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortKey = getString(R.string.sort_by_key);
        String defaultSort = getString(R.string.sort_by_popularity);
        String sortType=sharedPreferences.getString(sortKey,defaultSort);
        Uri builtUri = Uri.parse(MOVIE_DB_API).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String uriString = builtUri.toString();
        Log.d(TAG, "buildUri: Uri built is "+uriString);

        return uriString;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this,buildUri());
    }

    private void dismissLoadingIndicator(){
        View loadingIndicator = findViewById(com.example.android.popularmoviesapp.R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        dismissLoadingIndicator();
        mAdapter.clear();

        if(data!=null && !data.isEmpty()){
            mAdapter.addAll(data);
        }else {
            mEmptyTextView.setText(getString(R.string.no_movies));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
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


}
