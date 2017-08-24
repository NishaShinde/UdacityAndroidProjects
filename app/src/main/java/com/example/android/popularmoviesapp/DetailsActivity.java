/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.example.android.popularmoviesapp.Utils.PopularMoviesUtils;
import com.example.android.popularmoviesapp.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private ActivityDetailsBinding mBinding;

    private Movie myMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_details);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            myMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }

        if(myMovie != null){
            displayMovieDetails();
        }
    }

    private static void loadImage(ImageView view, String url,boolean isBackdrop) {
        String imageUrl = NetworkUtils.buildPosterPath(url,isBackdrop);
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.rectangle)
                .into(view);
        }

    private void displayMovieDetails() {

        loadImage(mBinding.backdropImageView, myMovie.getBackdropPath(), true);

        String movie = myMovie.getFormattedTitle();

        mBinding.layoutMovieInfo.tvMovieName.setText(movie);
        mBinding.layoutMovieInfo.tvMovieOverview.setText(myMovie.getOverview());
        mBinding.layoutMovieInfo.tvMovieRating.setText(PopularMoviesUtils.formatRating(myMovie.getRating()));
        mBinding.layoutMovieInfo.tvMovieReleaseDate.setText(PopularMoviesUtils.formatReleaseDate(myMovie.getReleaseDate()));

        mBinding.backdropImageView.setContentDescription("Backdrop of " + movie);
    }
}
