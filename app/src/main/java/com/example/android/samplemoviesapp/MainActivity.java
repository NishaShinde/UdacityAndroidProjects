package com.example.android.samplemoviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.samplemoviesapp.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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

        Uri builtUri = Uri.parse(MOVIE_DB_API).buildUpon()
                .appendPath(SORT_BY)
                .appendQueryParameter(QUERY_API_KEY,API_KEY)
                .build();

        Log.d(TAG, "Built Uri: "+builtUri.toString());

        new MovieDbQueryTask().execute(builtUri.toString());

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

    private class MovieDbQueryTask extends AsyncTask<String,Void,List<Movie>>{

        @Override
        protected List<Movie> doInBackground(String... url) {
            if(url.length==0 || url[0]==null){
                return null;
            }

            List<Movie> result = NetworkUtils.fetchPopularMovies(url[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mAdapter.clear();
            if(movies!=null && !movies.isEmpty()){
                mAdapter.addAll(movies);
            }
        }
    }
}
