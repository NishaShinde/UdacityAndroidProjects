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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private LinkedHashMap<String,String> mTrailers;
    final private OnTrailerClickListener onTrailerClickListener;

    public interface OnTrailerClickListener{
        void onTrailerClick(String key);
    }

    public TrailerAdapter(Context context, OnTrailerClickListener onTrailerClickListener){
        mContext = context;
        this.onTrailerClickListener = onTrailerClickListener;
        mTrailers = new LinkedHashMap<>();
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.trailer_item,parent,false);

        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailersData(LinkedHashMap<String,String> trailers){
        if(trailers == null) return;

        mTrailers.clear();
        mTrailers.putAll(trailers);
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
            ArrayList<String> keys = new ArrayList<>(mTrailers.keySet());

            String key = keys.get(position);
            String name = mTrailers.get(key);

            Log.d(TAG,"bind@TrailerViewHolder:#"+position+"-->"+key+" : "+name);

            mTrailersTextView.setText(name);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            ArrayList<String> keys = new ArrayList<>(mTrailers.keySet());

            String key = keys.get(position);
            onTrailerClickListener.onTrailerClick(key);
        }
    }
}
