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
    private Context context;
    private Picasso picasso;

    public MovieAdapter(Context context, List<Movie> movies){

        super(context,0,movies);
        this.context = context;
        picasso = Picasso.with(context);

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
        //Log.d(TAG, "Created Poster Link:" +poster);

        if(!TextUtils.isEmpty(poster)){
            picasso.load(poster).into(imageView);
        }

        return convertView;
    }

}
