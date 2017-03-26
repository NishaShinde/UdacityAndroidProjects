package com.example.android.samplemoviesapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MOVIE_LOADER_ID=1;

    private static final String MOVIE_DB_API = "https://api.themoviedb.org/3/movie";
    private static final String SORT_BY = "popular";
    private static final String QUERY_API_KEY = "api_key";
    private static final String API_KEY ="c9d1bf2e884bf01ce5e41d543111ad01";


    /**Sample URL to display movies sorted by popular
     *https://api.themoviedb.org/3/movie/popular?api_key=c9d1bf2e884bf01ce5e41d543111ad01
     */

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MovieAdapter(this,new ArrayList<Movie>());

        GridView gridView = (GridView) findViewById(R.id.movie_grid);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie currentMovie = mAdapter.getItem(position);
                Toast.makeText(MainActivity.this, currentMovie.getOriginal_title()
                        + " Clicked.!!!", Toast.LENGTH_SHORT).show();
            }
        });

        getLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
    }

    private static String buildUri(){
        Uri builtUri = Uri.parse(MOVIE_DB_API).buildUpon()
                .appendPath(SORT_BY)
                .appendQueryParameter(QUERY_API_KEY,API_KEY)
                .build();

        String uriString = builtUri.toString();
        Log.d(TAG, "buildUri: Uri built is "+uriString);

        return uriString;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this,buildUri());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
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
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        if(itemThatWasClicked == R.id.action_sort_by){
            Toast.makeText(this, "Sort By Menu Item Clicked.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
