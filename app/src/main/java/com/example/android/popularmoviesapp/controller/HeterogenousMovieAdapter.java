package com.example.android.popularmoviesapp.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.utils.NetworkUtils;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dell on 03/09/2017.
 */

public class HeterogenousMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_POSTER = 1;
    private static final int VIEW_TYPE_TRAILER = 2;
    private static final int VIEW_TYPE_REVIEW = 3;

    private static final String TAG = HeterogenousMovieAdapter.class.getSimpleName();

    private List<Object> mData;
    private Context mContext;
    private final MovieItemClickListener mMovieItemClickListener;

    public interface MovieItemClickListener{
        void onItemClick(Object item);
    }

    public HeterogenousMovieAdapter(Context context, MovieItemClickListener mMovieItemClickListener,List data){
        this.mMovieItemClickListener = mMovieItemClickListener;
        mData = data;
        mContext = context;
    }

    public void swapData(List data){
        if(data == null) return;

        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTrailersTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailersTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            Trailer trailer = (Trailer) mData.get(position);
            if(trailer == null) return;
            mTrailersTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trailer trailer = (Trailer) mData.get(position);
            mMovieItemClickListener.onItemClick(trailer);
        }
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthorTextView;
        private TextView mContentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        void bind(int position){
            Review review = (Review) mData.get(position);
            if (review == null) return;

            String author = review.getAuthor();
            String content = review.getContent();

            mAuthorTextView.setText(author);
            mContentTextView.setText(content);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            case VIEW_TYPE_POSTER:
                View v1 = inflater.inflate(R.layout.grid_movie_item,parent,false);
                viewHolder = new PosterViewHolder(v1);
                break;
            case VIEW_TYPE_TRAILER:
                View v2 = inflater.inflate(R.layout.trailer_item,parent,false);
                viewHolder = new TrailerViewHolder(v2);
                break;
            case VIEW_TYPE_REVIEW:
                View v3 = inflater.inflate(R.layout.review_item,parent,false);
                viewHolder = new ReviewViewHolder(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position) instanceof Movie){
            return VIEW_TYPE_POSTER;
        }else if(mData.get(position) instanceof Trailer){
            return VIEW_TYPE_TRAILER;
        }else if(mData.get(position) instanceof Review){
            return VIEW_TYPE_REVIEW;
        }else {
            return  -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_POSTER:
                ((PosterViewHolder) holder).bind(position);
                break;
            case VIEW_TYPE_TRAILER:
                ((TrailerViewHolder)holder).bind(position);
                break;
            case VIEW_TYPE_REVIEW:
                ((ReviewViewHolder)holder).bind(position);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported view Type found: "+holder.getItemViewType());
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }
        return mData.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            Movie movie = (Movie)mData.get(position);

            if(movie == null){
                return;
            }

            String poster = NetworkUtils.buildPosterPath(movie.getPoster(),false);
            //Title for setting content description
            String a11yMoviePoster = movie.getFormattedTitle();

            if(!TextUtils.isEmpty(poster)){
                Picasso.with(mContext)
                        .load(poster)
                        .placeholder(R.drawable.rectangle)
                        .into(mImageView);
                mImageView.setContentDescription(a11yMoviePoster);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = (Movie) mData.get(position);
            mMovieItemClickListener.onItemClick(movie);
        }
    }
}
