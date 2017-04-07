package com.example.android.samplemoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.samplemoviesapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private TextView mNameTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    //private TextView mDurationTextView;
    private TextView mRatingTextView;
    private TextView mOverviewTextView;
    private Button mFavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mPosterImageView = (ImageView)findViewById(R.id.posterImageView);
        mYearTextView = (TextView)findViewById(R.id.yearTextView);
        //mDurationTextView = (TextView)findViewById(R.id.durationTextView);
        mRatingTextView = (TextView)findViewById(R.id.ratingTextView);
        mOverviewTextView = (TextView)findViewById(R.id.overviewTextView);
        mFavButton = (Button)findViewById(R.id.favButton);

        Movie myParcelableMovie = (Movie) getIntent().getParcelableExtra("Current Movie");

        if(myParcelableMovie == null){
            Toast.makeText(this, "Oops!!! Something went wrong :( Please try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Null object received for movie: "+myParcelableMovie.toString());
        }

        Log.d(TAG, "Current Movie: "+myParcelableMovie.toString());

        mNameTextView.setText(myParcelableMovie.getOriginal_title());

        String posterPath = NetworkUtils.buildPosterPath(myParcelableMovie.getPoster_path());
        Picasso.with(this).load(posterPath).into(mPosterImageView);

        mRatingTextView.setText(Integer.toString(myParcelableMovie.getUser_rating()));
        mOverviewTextView.setText(myParcelableMovie.getPlot_synopsis());
        mYearTextView.setText(myParcelableMovie.getRelease_date());
    }
}
