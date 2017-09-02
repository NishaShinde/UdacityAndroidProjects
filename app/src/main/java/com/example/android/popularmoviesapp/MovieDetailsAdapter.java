

package com.example.android.popularmoviesapp;

/**
 * Created by dell on 27/08/2017.
 */
/**
public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieDetailsAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_POSTER_GRID = 1;
    private static final int VIEW_TYPE_TRAILERS = 2;
    private static final int VIEW_TYPE_REVIEWS = 3;

    private Object mData;
    private Context mContext;

    final private MovieItemClickListener mMovieItemClickListener;

    public interface MovieItemClickListener {
        void onMovieItemClick(Object item);
    }

    public MovieDetailsAdapter(Context context, Object data, MovieItemClickListener mMovieItemClickListener) {
        mContext = context;
        mData = data;
        this.mMovieItemClickListener = mMovieItemClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (mData instanceof ArrayList) {
            return VIEW_TYPE_POSTER_GRID;
        } else if (mData instanceof ArrayMap) {
            ArrayMap<String,LinkedHashMap<String, String>> content = (ArrayMap<String,LinkedHashMap<String, String>>) mData;
            if (content.containsKey(NetworkUtils.TRAILERS)) {
                return VIEW_TYPE_TRAILERS;
            } else if (content.containsKey(NetworkUtils.REVIEWS)) {
                return VIEW_TYPE_REVIEWS;
            } else {
                throw new IllegalArgumentException("Unknown view type found.");
            }
        } else {
            throw new IllegalArgumentException("Unknown Object found.");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType){
            case VIEW_TYPE_POSTER_GRID:
                View viewPoster = inflater.inflate(R.layout.grid_movie_item,parent,false);
                viewHolder = new PosterViewHolder(viewPoster);
                break;
            case VIEW_TYPE_TRAILERS:
                View viewTrailer = inflater.inflate(R.layout.trailer_item,parent,false);
                viewHolder = new TrailerViewHolder(viewTrailer);
                break;
            case VIEW_TYPE_REVIEWS:
                View viewReview = inflater.inflate(R.layout.review_item,parent,false);
                viewHolder = new ReviewViewHolder(viewReview);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type found."+viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_POSTER_GRID:
                PosterViewHolder viewHolder = (PosterViewHolder) holder;
                ((PosterViewHolder) holder).bind(viewHolder,position);
                break;
            case VIEW_TYPE_TRAILERS:
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder)holder;
                trailerViewHolder.bind(trailerViewHolder,position);
                break;
            case VIEW_TYPE_REVIEWS:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder)holder;
                reviewViewHolder.bind(reviewViewHolder,position);
                break;
            default:
                throw new IllegalArgumentException("Unsupported itemViewType found: "+holder.getItemViewType());
        }

    }

    @Override
    public int getItemCount() {
        if (mData instanceof ArrayList) {
            return ((ArrayList) mData).size();
        } else if (mData instanceof ArrayMap) {
            ArrayMap<String,LinkedHashMap<String, String>> content = (ArrayMap<String,LinkedHashMap<String, String>>) mData;
            if (content.containsKey(NetworkUtils.TRAILERS)) {
                LinkedHashMap<String,String> trailers = content.get(NetworkUtils.TRAILERS);
                return trailers.size();
            } else if (content.containsKey(NetworkUtils.REVIEWS)) {
                LinkedHashMap<String,String> reviews = content.get(NetworkUtils.REVIEWS);
                return reviews.size();
            } else {
                throw new IllegalArgumentException("Unknown view type found.");
            }
        } else {
            throw new IllegalArgumentException("Unknown Object found.");
        }
    }

    public void setPostersData(List<Movie> movies){

       if(movies == null) return;

        mData = movies;

        (ArrayList)(mData).clear();
        moviesList.addAll(movies);
        notifyDataSetChanged();
    }

    public void setTrailersData(LinkedHashMap<String,String> trailers){
        if(trailers == null) return;

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
            mMovieItemClickListener.onMovieItemClick(key);
        }
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(RecyclerView.ViewHolder holder, int position) {
            ArrayList<Movie> movies = (ArrayList<Movie>)mData;

            Movie movie = ((ArrayList<Movie>) mData).get(position);
            if (movie == null) {
                return;
            }

            String poster = NetworkUtils.buildPosterPath(movie.getPoster(), false);
            //Title for setting content description
            String a11yMoviePoster = movie.getFormattedTitle();

            if (!TextUtils.isEmpty(poster)) {
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
            ArrayList<Movie> movies = (ArrayList) mData;
            Movie movie = movies.get(position);
            mMovieItemClickListener.onMovieItemClick(movie);
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