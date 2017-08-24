/*
 * Copyright (C) 2017 Udacity Android Nanodegree Popular Movies Project
 */


package com.example.android.popularmoviesapp.Utils;

import android.net.Uri;
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
import java.util.List;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_IMAGE_SIZE  = "w185";
    private static final String BACKDROP_IMAGE_SIZE="w500";

    private NetworkUtils(){}

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
        URL url = createUrl(requestUrl);
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException io){
            Log.e(TAG, "fetchPopularMovies: Exception caught in making HTTP request");
        }

        return extractMoviesFromJson(jsonResponse);
    }

    private static List<Movie> extractMoviesFromJson(String response){
        if(TextUtils.isEmpty(response)){
            return null;
        }

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
}
