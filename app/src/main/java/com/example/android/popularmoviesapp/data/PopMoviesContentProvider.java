package com.example.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dell on 13/09/2017.
 */

public class PopMoviesContentProvider extends ContentProvider {

    public static final int FAVORITE_MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private PopMoviesDbHelper mDbHelper;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopMoviesContract.AUTHORITY,PopMoviesContract.PATH_FAVORITE_MOVIES,FAVORITE_MOVIES);
        uriMatcher.addURI(PopMoviesContract.AUTHORITY,PopMoviesContract.PATH_FAVORITE_MOVIES + "/#",MOVIE_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new PopMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITE_MOVIES:
                long id = db.insert(PopMoviesContract.FavoriteMoviesEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(PopMoviesContract.FavoriteMoviesEntry.CONTENT_URI,id);
                }else {
                    throw new SQLException("Failed to insert new row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri found: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
