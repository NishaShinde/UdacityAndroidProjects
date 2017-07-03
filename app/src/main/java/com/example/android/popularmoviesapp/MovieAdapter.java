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

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private Picasso mPicasso;

    public MovieAdapter(Context context, List<Movie> movies){

        super(context,0,movies);
        mContext = context;
        mPicasso = Picasso.with(mContext);

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

        String poster = NetworkUtils.buildPosterPath(currentMovie.getPoster());

        if(!TextUtils.isEmpty(poster)){
            mPicasso.load(poster).into(imageView);
        }

        return convertView;
    }

}
