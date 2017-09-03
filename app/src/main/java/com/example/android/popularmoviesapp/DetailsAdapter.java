package com.example.android.popularmoviesapp;

/**
 * Created by dell on 31/08/2017.
 */

/**
public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TRAILERS = 7;
    private static final int VIEW_TYPE_REVIEWS = 8;

    private LinkedHashMap<String,String> mContentMap;
    private DetailsItemClickListener mDetailsItemClickListener;
    private Context mContext;

    public DetailsAdapter(Context context,DetailsItemClickListener detailsItemClickListener){
        mContext = context;
        mDetailsItemClickListener = detailsItemClickListener;
        mContentMap = new LinkedHashMap<>();
    }

    public interface DetailsItemClickListener{
        void onDetailsItemClick(String key,String value);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mContentMap.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTrailersTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailersTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        void bind(RecyclerView.ViewHolder viewHolder,int position){

            ArrayMap<String,LinkedHashMap<String,String>> map = (ArrayMap<String,LinkedHashMap<String,String>>)mData;

            LinkedHashMap<String,String> trailersMap = map.get(NetworkUtils.TRAILERS);

            ArrayList<String> keys = new ArrayList<>(trailersMap.keySet());

            String key = keys.get(position);
            String name = trailersMap.get(key);

            mTrailersTextView.setText(name+":"+key);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ArrayMap<String,LinkedHashMap<String,String>> map = (ArrayMap<String,LinkedHashMap<String,String>>)mData;

            LinkedHashMap<String,String> trailersMap = map.get(NetworkUtils.TRAILERS);

            ArrayList<String> keys = new ArrayList<>(trailersMap.keySet());

            String key = keys.get(position);
            //mMovieItemClickListener.onMovieItemClick(key);
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        private TextView mAuthorTextView;
        private TextView mContentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        void bind(RecyclerView.ViewHolder viewHolder,int position){

            ArrayMap<String,LinkedHashMap<String,String>> map = (ArrayMap<String,LinkedHashMap<String,String>>)mData;

            LinkedHashMap<String,String> reviewsMap = map.get(NetworkUtils.REVIEWS);

            ArrayList<String> authors = new ArrayList<>(reviewsMap.keySet());

            String author = authors.get(position);
            String content = reviewsMap.get(author);

            mAuthorTextView.setText(author);
            mContentTextView.setText(content);
        }
    }
}
*/