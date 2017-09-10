package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell on 04/09/2017.
 */

public class PopMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public PopMoviesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIES =
                "CREATE TABLE " + PopMoviesContract.MoviesEntry.TABLE_NAME + " (" +
                        PopMoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PopMoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        PopMoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        PopMoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        PopMoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                        PopMoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        PopMoviesContract.MoviesEntry.COLUMN_RATING + " REAL NOT NULL,  " +
                        PopMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" +
                        " );";

        db.execSQL(CREATE_TABLE_MOVIES);

        final String CREATE_TABLE_TRAILERS =
                "CREATE TABLE " + PopMoviesContract.TrailerEntry.TABLE_NAME + " ( " +
                        PopMoviesContract.TrailerEntry._ID + " INTEGER PRIMARY KET AUTOINCREMENT, " +
                        PopMoviesContract.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        PopMoviesContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                        PopMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL" +
                        " );";

        db.execSQL(CREATE_TABLE_TRAILERS);

        final String CREATE_TABLE_REVIEWS =
                "CREATE TABLE " + PopMoviesContract.ReviewEntry.TABLE_NAME + " ( " +
                        PopMoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KET AUTOINCREMENT, " +
                        PopMoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        PopMoviesContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        PopMoviesContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL" +
                        " );";

        db.execSQL(CREATE_TABLE_REVIEWS);

        final String CREATE_TABLE_FAVORITES =
                "CREATE TABLE " + PopMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " ( " +
                        PopMoviesContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KET AUTOINCREMENT, " +
                        PopMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL" +
                        " );";

        db.execSQL(CREATE_TABLE_FAVORITES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
