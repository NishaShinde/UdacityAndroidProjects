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

/**
 * Created by dell on 11/03/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE  = "w185";

    private NetworkUtils(){};

    public static String buildPosterPath(String poster){
        String result = null;

        if(!TextUtils.isEmpty(poster)){
            result = Uri.parse(IMAGE_BASE_URL).buildUpon()
                    .appendPath(IMAGE_SIZE)
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

        List<Movie> popularMovies = extractMoviesFromJson(jsonResponse);
        return popularMovies;
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
                String poster_path = currentMovie.getString("poster_path");
                String original_title = currentMovie.getString("original_title");
                String overview = currentMovie.getString("overview");
                int user_rating = currentMovie.getInt("vote_average");
                String release_date = currentMovie.getString("release_date");

                Movie movie = new Movie();
                movie.setPoster_path(poster_path);
                movie.setOriginal_title(original_title);
                movie.setPlot_synopsis(overview);
                movie.setUser_rating(user_rating);
                movie.setRelease_date(release_date);

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