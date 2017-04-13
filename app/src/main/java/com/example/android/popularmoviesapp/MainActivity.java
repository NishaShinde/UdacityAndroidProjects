package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mEmptyTextView;

    private String sortType="popular";

    private static final int MOVIE_LOADER_ID=1;
    private static final int MOVIE_LOADER_ID_TOP_RATED=2;

    private static final String MOVIE_DB_API = "https://api.themoviedb.org/3/movie";
    private static final String QUERY_API_KEY = "api_key";


    /**Sample URL to display movies sorted by popular
     *https://api.themoviedb.org/3/movie/popular?api_key=c9d1bf2e884bf01ce5e41d543111ad01
     */

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.popularmoviesapp.R.layout.activity_main);

        mEmptyTextView = (TextView)findViewById(com.example.android.popularmoviesapp.R.id.emptyTextView);

        mAdapter = new MovieAdapter(this,new ArrayList<Movie>());

        GridView gridView = (GridView) findViewById(com.example.android.popularmoviesapp.R.id.movie_grid);
        gridView.setEmptyView(mEmptyTextView);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie currentMovie = mAdapter.getItem(position);
                Intent movieIntent = new Intent(getApplicationContext(),DetailsActivity.class);
                movieIntent.putExtra("Current Movie",currentMovie);
                startActivity(movieIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            getLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        }else{
            dismissLoadingIndicator();
            mEmptyTextView.setText(com.example.android.popularmoviesapp.R.string.no_internet);
        }
    }

    private String buildUri(){
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
        mEmptyTextView.setText(com.example.android.popularmoviesapp.R.string.no_movies);
        mAdapter.clear();
        if(data!=null && !data.isEmpty()){
            mAdapter.addAll(data);
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
        if(itemThatWasClicked == com.example.android.popularmoviesapp.R.id.action_popularity){
            sortType="populer";
            getLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        }else if(itemThatWasClicked == com.example.android.popularmoviesapp.R.id.action_top_rated){
            sortType="top_rated";
            getLoaderManager().initLoader(MOVIE_LOADER_ID_TOP_RATED,null,this);
        }
        return super.onOptionsItemSelected(item);
    }
}
