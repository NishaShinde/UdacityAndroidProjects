package com.example.android.popularmoviesapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dell on 24/08/2017.
 */

public final class PopularMoviesUtils {

    public static String formatReleaseDate(String date){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat formatter = new SimpleDateFormat("d MMM yyyy",Locale.getDefault());
        try {
            Date convertedDate = parser.parse(date);
            return formatter.format(convertedDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatRating(float rating){
        return Float.toString(rating) +
                " / 10";
    }

    public static void watchYouTubeVideo(Context context,String trailerKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW,NetworkUtils.buildYouTubeAppUri(trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,NetworkUtils.buildYouTubeWebUri(trailerKey));

        try{
            context.startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            context.startActivity(webIntent);
        }
    }

}
