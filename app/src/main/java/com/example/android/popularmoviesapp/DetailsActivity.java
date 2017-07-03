package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.example.android.popularmoviesapp.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

     ActivityDetailsBinding mBinding;

    private Movie myMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_details);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            myMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            //Log.d(TAG,"Clicked movie: "+myMovie.toString());
        }

        displayMovieDetails(myMovie);
    }

    public static void loadImage(ImageView view, String url) {
        String imageUrl = NetworkUtils.buildPosterPath(url);
        Picasso.with(view.getContext()).load(imageUrl).into(view);
    }

    private void displayMovieDetails(Movie movie){

        if(movie != null){
            loadImage(mBinding.posterImageView,movie.getPoster());
            loadImage(mBinding.backdropImageView,movie.getBackdropPath());
            mBinding.movieNameTextView.setText(movie.getFormattedTitle());
            mBinding.movieOverviewTextView.setText(movie.getOverview());
            mBinding.movieRatingTextView.setText(formatRating(movie.getRating()));
            mBinding.releaseDateTextView.setText(formatDate(movie.getReleaseDate()));
        }

    }

    private String formatDate(String date){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat formatter = new SimpleDateFormat("d MMM yyyy",Locale.getDefault());
        try {
            Date convertedDate = parser.parse(date);
            return formatter.format(convertedDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatRating(float rating){
        return new StringBuilder(Float.toString(rating))
                .append(" / 10")
                .toString();
    }

}
