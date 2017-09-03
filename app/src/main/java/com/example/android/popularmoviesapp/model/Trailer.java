package com.example.android.popularmoviesapp.model;

/**
 * Created by dell on 03/09/2017.
 */

public class Trailer {

    private String key;
    private String name;

    public Trailer(String key,String name){
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
