package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dell on 02/09/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private Context mContext;
    private LinkedHashMap<String,String> mReviews;

    public ReviewAdapter(Context context) {
        mContext = context;
        mReviews = new LinkedHashMap<>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.review_item,parent,false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviewData(LinkedHashMap<String,String> reviews) {

        if(reviews == null) return;

        mReviews.clear();
        mReviews.putAll(reviews);
        notifyDataSetChanged();
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

            ArrayList<String> authors = new ArrayList<>(mReviews.keySet());

            String author = authors.get(position);
            String content = mReviews.get(author);

            Log.d(TAG,"bind@ReviewViewHolder:#"+position+"-->"+author+" : "+content);

            mAuthorTextView.setText(author);
            mContentTextView.setText(content);
        }
    }
}
