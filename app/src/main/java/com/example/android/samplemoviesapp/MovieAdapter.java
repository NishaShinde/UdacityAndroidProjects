package com.example.android.samplemoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.samplemoviesapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private Picasso sPicasso;

    public MovieAdapter(Context context, List<Movie> movies){

        super(context,0,movies);
        mContext = context;
        sPicasso = Picasso.with(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie currentMovie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image);

        String poster = NetworkUtils.buildPosterPath(currentMovie.getPosterPath());

        if(poster!=null && !poster.equals("")){
            sPicasso.load(poster).into(imageView);
        }

        return convertView;
    }

}
