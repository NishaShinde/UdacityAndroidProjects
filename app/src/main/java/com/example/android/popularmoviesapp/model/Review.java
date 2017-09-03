package com.example.android.popularmoviesapp.model;

/**
 * Created by dell on 03/09/2017.
 */

public class Review {

    private String author;
    private String content;

    public Review(String author, String content){
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
