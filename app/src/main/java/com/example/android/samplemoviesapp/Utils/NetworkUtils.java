package com.example.android.samplemoviesapp.Utils;

/**
 * Created by dell on 11/03/2017.
 */

public final class NetworkUtils {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE  = "w185/";

    private NetworkUtils(){};

    public static String buildPosterPath(String poster){
        StringBuilder sb = new StringBuilder(IMAGE_BASE_URL);
        String result=null;
        if((poster.length()>0) && (! poster.equals(""))){
            result = sb.append(IMAGE_SIZE).
                    append(poster).toString();
        }
        return result;
    }
}
