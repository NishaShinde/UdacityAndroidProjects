/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */
package com.example.android.popularmoviesapp.view;

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
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.BuildConfig;
import com.example.android.popularmoviesapp.controller.HeterogenousMovieAdapter;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.utils.NetworkUtils;
import com.example.android.popularmoviesapp.utils.SpacesItemDecoration;
import com.example.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List>,SharedPreferences.OnSharedPreferenceChangeListener,HeterogenousMovieAdapter.MovieItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MOVIES_LOADER_ID=0;
    private static boolean PREFERENCES_HAVE_BEEN_CHANGED;

    private HeterogenousMovieAdapter mAdapter;
    //private MovieAdapter mAdapter;
    private TextView mEmptyTextView;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private static final int NUM_OF_COLUMNS = 2;
    private static final int DEFAULT_SCROLL_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.popularmoviesapp.R.layout.activity_main);

        mEmptyTextView = (TextView)findViewById(R.id.emptyTextView);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);

        initRecyclerView();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void initRecyclerView() {

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this,NUM_OF_COLUMNS);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new HeterogenousMovieAdapter(this,this,new ArrayList());
        mRecyclerView.setAdapter(mAdapter);
        //attachSnapping();
        //addDivider();
        loadMovieData();
    }

    private void addDivider(){
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(4);
        mRecyclerView.addItemDecoration(spacesItemDecoration);
    }

    /*
    * Helper method: attachSnapping() renders
    * default snapping behaviour of snapping to center.
     */

    private void attachSnapping() {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
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
    public Loader<List> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List>(this) {

            List<Movie> movieList;

            @Override
            protected void onStartLoading() {
                if(movieList != null){
                    deliverResult(movieList);
                }else{
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(List data) {
                List<Movie> oldData = movieList;
                movieList = data;

                super.deliverResult(data);
            }

            @Override
            public List loadInBackground() {
                String uri = buildUri();

                if(uri == null){
                    return null;
                }
                return NetworkUtils.fetchPopularMovies(uri);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {

        dismissLoadingIndicator();

        if(data!=null && !data.isEmpty()){
            mAdapter.swapData(data);
            //After loading data, set the default scroll position.
            mRecyclerView.scrollToPosition(DEFAULT_SCROLL_POSITION);
        }else {
            Log.d(TAG,"Caught in No Movies found."+data.size());
            showErrorMessage();
            mEmptyTextView.setText(getString(R.string.no_movies));
        }
    }

    @Override
    public void onLoaderReset(Loader<List> loader) {
        mAdapter.swapData(null);
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

    private String buildUri(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortKey = getString(R.string.sort_by_key);
        String defaultSort = getString(R.string.sort_by_popularity);
        String sortType=sharedPreferences.getString(sortKey,defaultSort);
        Uri builtUri = Uri.parse(getString(R.string.movie_dp_api)).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(getString(R.string.query_api_key), BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        return builtUri.toString();
    }

    private void loadMovieData() {
        if(isNetworkAvailable()){
            getLoaderManager().initLoader(MOVIES_LOADER_ID,null,this);
        }else{
            showErrorMessage();
            mEmptyTextView.setText(getString(R.string.no_internet));
        }
    }

    private void dismissLoadingIndicator(){
        mEmptyTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showErrorMessage(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onItemClick(Object item) {
        if(item == null) return;

        if(item instanceof Movie){
            Movie movie = (Movie) item;
            Intent movieIntent = new Intent(MainActivity.this,DetailsActivity.class);
            movieIntent.putExtra(Intent.EXTRA_TEXT,movie);
            startActivity(movieIntent);
        }

    }
}
