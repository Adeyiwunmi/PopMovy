package com.jp.popularmoviess1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LENIOVO on 7/2/2017.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favMovies.db";


    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        addFavMoviesTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addFavMoviesTable(SQLiteDatabase db) {

        String createTableSqlStateMent = "CREATE TABLE " + FavoriteMoviesContract.FavMovieEntry.TABLE_NAME +
                "(" + FavoriteMoviesContract.FavMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_POSTER_URL + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL);";

        db.execSQL(createTableSqlStateMent);

    }
}
