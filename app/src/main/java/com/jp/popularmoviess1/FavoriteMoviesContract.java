package com.jp.popularmoviess1;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.widget.StackView;

/**
 * Created by LENIOVO on 7/2/2017.
 */

public class FavoriteMoviesContract {
    public static String CONTENT_AUTHORITY = "com.jp.popularmoviess1.FavMoviesContentProvider";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movies";


    public  static final class FavMovieEntry implements BaseColumns {

       public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE  =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "favouriteMovies";
       public static final String COLUMN_MOVIE_TITLE = "title";
        public static final  String COLUMN_MOVIE_RELEASE_DATE= "releaseDate";
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        public static final String COLUMN_MOVIE_POSTER_URL = "moviePosterUrl";
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        public static final String COLUMN_MOVIE_ID = "movieID";



        public  static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}
