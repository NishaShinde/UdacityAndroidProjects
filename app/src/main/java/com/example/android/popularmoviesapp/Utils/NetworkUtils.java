/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp.Utils;

import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesapp.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch";
    private static final String POSTER_IMAGE_SIZE  = "w185";
    private static final String BACKDROP_IMAGE_SIZE="w500";

    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";

    private NetworkUtils(){}

    public static Uri buildYouTubeAppUri(String key){
        if(TextUtils.isEmpty(key)) return null;

        return Uri.parse("vnd.youtube:"+key);
    }

    public static Uri buildYouTubeWebUri(String key){
        if(TextUtils.isEmpty(key)) return null;

        Uri result = null;

        result = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter("v",key).build();

        return result;
    }

    public static String buildPosterPath(String poster,boolean isBackdrop){
        String result = null;

        String imageSize = isBackdrop ? BACKDROP_IMAGE_SIZE : POSTER_IMAGE_SIZE;

        if(!TextUtils.isEmpty(poster)){
            result = Uri.parse(IMAGE_BASE_URL).buildUpon()
                    .appendPath(imageSize)
                    .appendEncodedPath(poster).build().toString();
        }
        return result;
    }

    public static List<Movie> fetchPopularMovies(String requestUrl){

        String jsonResponse = getJsonResponse(requestUrl);

        if(TextUtils.isEmpty(jsonResponse)){
            throw new IllegalArgumentException("Empty json Response obtained for URL: " + requestUrl);
        }

        return extractMoviesFromJson(jsonResponse);
    }

    private static List<Movie> extractMoviesFromJson(String response){

        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(response);
            JSONArray results = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentMovie = results.getJSONObject(i);
                long _id = currentMovie.getInt("id");
                String poster_path = currentMovie.getString("poster_path");
                String backdrop_path = currentMovie.getString("backdrop_path");
                String original_title = currentMovie.getString("original_title");
                String overview = currentMovie.getString("overview");
                String rating = currentMovie.getString("vote_average");
                String release_date = currentMovie.getString("release_date");

                Movie movie = new Movie(_id);
                movie.setPoster(poster_path);
                movie.setBackdropPath(backdrop_path);
                movie.setTitle(original_title);
                movie.setOverview(overview);
                movie.setRating(Float.parseFloat(rating));
                movie.setReleaseDate(release_date);

                movies.add(movie);
                Log.v(TAG,"Movie added: "+movie.toString());
            }
        }catch (JSONException e){
            Log.e(TAG, "extractMoviesFromJson: Exception parsing JSON Data");
        }
        return movies;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Exception caught in creating the URL for "+stringUrl);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        if(urlConnection.getResponseCode()==200){
            InputStream inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }else{
            Log.e(TAG, "makeHttpRequest: Response received from the server "+urlConnection.getResponseCode());
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        if(is != null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

     private static String getJsonResponse(String requestUrl){
         URL url = createUrl(requestUrl);
         String jsonResponse = "";

         try {
             jsonResponse = makeHttpRequest(url);
         }catch (IOException io){
             Log.e(TAG, "getJsonResponse: Exception caught in making HTTP request for URL:"+requestUrl);
         }
         return jsonResponse;

     }

    public static ArrayMap<String,LinkedHashMap<String,String>> getTrailersAndReviews(String requestUrl){

        String jsonResponse = getJsonResponse(requestUrl);

        if(TextUtils.isEmpty(jsonResponse)){
            throw new IllegalArgumentException("Empty json Response obtained for URL: " + requestUrl);
        }

        ArrayMap<String,LinkedHashMap<String,String>> results = new ArrayMap<>(2);

        LinkedHashMap<String,String> trailers = extractTrailersFromResponse(jsonResponse);
        LinkedHashMap<String,String> reviews = extractReviewsFromResponse(jsonResponse);

        results.put(TRAILERS,trailers);
        results.put(REVIEWS,reviews);

        return results;
    }

    private static LinkedHashMap<String,String> extractReviewsFromResponse(String jsonResponse) {

        LinkedHashMap<String,String> reviewsMap = new LinkedHashMap<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray reviews = baseJsonResponse.getJSONArray("reviews");
            JSONArray results = reviews.getJSONArray(0);
            for (int i=0;i<results.length();i++){
                JSONObject review = results.getJSONObject(i);
                String author = review.getString("author");
                JSONArray contentArray = review.getJSONArray("content");
                String content = contentArray.getString(0);
                reviewsMap.put(author,content);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewsMap;
    }

    private static LinkedHashMap<String,String> extractTrailersFromResponse(String jsonResponse) {

        LinkedHashMap<String,String> trailers = new LinkedHashMap<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray videos = baseJsonResponse.getJSONArray("videos");
            JSONArray results = videos.getJSONArray(0);
            for (int i=0;i<results.length();i++){
                JSONObject trailer = results.getJSONObject(i);
                String key = trailer.getString("key");
                String name = trailer.getString("name");
                trailers.put(key,name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }

}
