/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */

package com.example.android.popularmoviesapp.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.BuildConfig;
import com.example.android.popularmoviesapp.controller.HeterogenousMovieAdapter;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.utils.NetworkUtils;
import com.example.android.popularmoviesapp.utils.PopularMoviesUtils;
import com.example.android.popularmoviesapp.databinding.ActivityDetailsBinding;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayMap<String,List>>,HeterogenousMovieAdapter.MovieItemClickListener {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private ActivityDetailsBinding mBinding;
    private HeterogenousMovieAdapter mTrailersAdapter;
    private HeterogenousMovieAdapter mReviewsAdapter;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private Movie myMovie;

    private static final int DETAILS_LOADER_ID = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_details);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            myMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }

        if(myMovie == null){
            throw new NullPointerException("Null Movie found from intent");
        }

        mTrailersAdapter = new HeterogenousMovieAdapter(this,this,new ArrayList());
        mReviewsAdapter = new HeterogenousMovieAdapter(this,this,new ArrayList());

        initTrailerRecyclerView(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false),mTrailersAdapter);
        initReviewRecyclerView(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false),mReviewsAdapter);
        displayMovieDetails();
        getSupportLoaderManager().initLoader(DETAILS_LOADER_ID,null,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID,null,this);
    }

    private RecyclerView.ItemDecoration addDivider(){
        RecyclerView.ItemDecoration decorator = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        return decorator;
    }

    private void initTrailerRecyclerView(RecyclerView.LayoutManager layoutManager, HeterogenousMovieAdapter adapter){
        mTrailersRecyclerView = (RecyclerView)findViewById(R.id.rv_trailers);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setAdapter(adapter);
        mTrailersRecyclerView.addItemDecoration(addDivider());
    }

    private void initReviewRecyclerView(RecyclerView.LayoutManager layoutManager,HeterogenousMovieAdapter adapter){
        mReviewsRecyclerView = (RecyclerView)findViewById(R.id.rv_reviews);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setAdapter(adapter);
        mReviewsRecyclerView.addItemDecoration(addDivider());
    }

    private String createUriForMovieDetails(long id) {

        Uri builtUri = Uri.parse(getString(R.string.movie_dp_api)).buildUpon()
                .appendPath(id+"")
                .appendQueryParameter(getString(R.string.query_api_key), BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(getString(R.string.query_append_to_response),getString(R.string.literals_for_videos_reviews))
                .build();

        return builtUri.toString();
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
        mBinding.backdropImageView.setContentDescription("Backdrop image of " + movie);

        mBinding.layoutMovieInfo.tvMovieName.setText(movie);
        mBinding.layoutMovieInfo.tvMovieOverview.setText(myMovie.getOverview());
        mBinding.layoutMovieInfo.tvMovieRating.setText(PopularMoviesUtils.formatRating(myMovie.getRating()));
        mBinding.layoutMovieInfo.tvMovieReleaseDate.setText(PopularMoviesUtils.formatReleaseDate(myMovie.getReleaseDate()));

    }

    @Override
    public Loader<ArrayMap<String, List>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayMap<String, List>>(this) {
            ArrayMap<String,List> trailersReviews;

            @Override
            protected void onStartLoading() {
                if(trailersReviews != null){
                    deliverResult(trailersReviews);
                }else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(ArrayMap<String, List> data) {
                ArrayMap<String,List> old = trailersReviews;
                trailersReviews = data;
                super.deliverResult(data);
            }

            @Override
            public ArrayMap<String, List> loadInBackground() {

                long _id = myMovie.get_id();
                String requestString = createUriForMovieDetails(_id);
                return NetworkUtils.getTrailersAndReviews(requestString);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayMap<String,List>> loader, ArrayMap<String, List> data) {

        if(data==null || data.isEmpty()){
            throw new IllegalArgumentException("Null or Empty map retrieved.");
        }

        List<Trailer> trailers = data.get(NetworkUtils.TRAILERS);
        List<Review> reviews = data.get(NetworkUtils.REVIEWS);

        mTrailersAdapter.swapData(trailers);
        mReviewsAdapter.swapData(reviews);

        //List<Object> trailersAndReviews = new ArrayList<>();
        //trailersAndReviews.addAll(trailers);
        //trailersAndReviews.addAll(reviews);

        //mAdapter.swapData(trailersAndReviews);
    }

    @Override
    public void onLoaderReset(Loader<ArrayMap<String,List>> loader) {
        mTrailersAdapter.swapData(null);
        mReviewsAdapter.swapData(null);
    }

    private void watchYouTubeVideo(String trailerKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW,NetworkUtils.buildYouTubeAppUri(trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,NetworkUtils.buildYouTubeWebUri(trailerKey));

        try{
            startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    @Override
    public void onItemClick(Object item) {
        if(item == null) return;

        if(item instanceof Trailer){
            Trailer trailer = (Trailer) item;
            String key = trailer.getKey();
            watchYouTubeVideo(key);
        }
    }
}
