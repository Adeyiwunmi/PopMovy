package com.jp.popularmoviess1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by LENIOVO on 7/2/2017.
 */

public class FavMoviesContentProvider extends ContentProvider {
     private static final  int FAV_MOVIE = 200;
    private static final int FAV_MOVIE_ID = 201;
    private static final UriMatcher sUrimatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper favoriteMoviesDbHelper;



    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.PATH_MOVIE,FAV_MOVIE);
        uriMatcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.PATH_MOVIE + "/#", FAV_MOVIE_ID);
        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
    favoriteMoviesDbHelper = new FavoriteMoviesDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = favoriteMoviesDbHelper.getWritableDatabase();
        Cursor returnCursor ;

        switch (sUrimatcher.match(uri)){
            case FAV_MOVIE:
                returnCursor = db.query(FavoriteMoviesContract.FavMovieEntry.TABLE_NAME,
                        null,null,null, null, null, null);
                break;

            default:    throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }


       returnCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
       return  null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
      final  SQLiteDatabase db = favoriteMoviesDbHelper.getWritableDatabase();
        long _id =0;
        Uri returnUri = null;


        switch (sUrimatcher.match(uri)){
            case FAV_MOVIE:
                _id = (db.insert(FavoriteMoviesContract.FavMovieEntry.TABLE_NAME, null, values));

                if(_id>0){
                   // returnUri = FavoriteMoviesContract.FavMovieEntry.buildMovieUri(_id);
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavMovieEntry.CONTENT_URI,_id);
                } else    {
                    throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
                    break;

            default:    throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
             getContext().getContentResolver().notifyChange(uri, null);
         return returnUri;



    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = favoriteMoviesDbHelper.getWritableDatabase();
        int rows;
        int match = sUrimatcher.match(uri);
        switch (sUrimatcher.match(uri)){
            case FAV_MOVIE:
                rows = db.delete(FavoriteMoviesContract.FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:   throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (selection==null  || rows!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
