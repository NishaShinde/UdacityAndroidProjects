package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Picasso mPicasso;

    public MovieAdapter(Context context, List<Movie> movies){

        super(context,0,movies);
        mPicasso = Picasso.with(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie currentMovie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    com.example.android.popularmoviesapp.R.layout.grid_item,parent,false);
        }


        ImageView imageView = (ImageView)convertView.findViewById(com.example.android.popularmoviesapp.R.id.movie_image);


        if(currentMovie == null){
            return null;
        }

        String poster = NetworkUtils.buildPosterPath(currentMovie.getPoster());

        //Title for setting content description
        String title = currentMovie.getFormattedTitle();

        if(!TextUtils.isEmpty(poster)){
            mPicasso.load(poster).into(imageView);
            /*
              Added Content Descriptions for image view
             */
            imageView.setContentDescription(title);
        }

        return convertView;
    }

}
