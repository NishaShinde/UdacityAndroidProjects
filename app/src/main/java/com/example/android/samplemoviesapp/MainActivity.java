package com.example.android.samplemoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("/aybgjbFbn6yUbsgUMnUbwc2jcWd.jpg"));
        movies.add(new Movie("/45Y1G5FEgttPAwjTYic6czC9xCn.jpg"));
        movies.add(new Movie("/z09QAf8WbZncbitewNk6lKYMZsh.jpg"));
        movies.add(new Movie("/bbxtz5V0vvnTDA2qWbiiRC77Ok9.jpg"));

        mMoviesAdapter = new MovieAdapter(this,movies);

        GridView gridView = (GridView) findViewById(R.id.movie_grid);
        gridView.setAdapter(mMoviesAdapter);
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
