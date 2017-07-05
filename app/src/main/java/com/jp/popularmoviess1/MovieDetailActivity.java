package com.jp.popularmoviess1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Trace;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {
@BindView(R.id.idMovieReleaseDate) TextView releaseDateTV;
    @BindView(R.id.idMovieRating) TextView ratingTV;
    @BindView(R.id.idMovieOverview) TextView overviewTv;
    @BindView(R.id.idMovieTitle) TextView titleTv;
    @BindView(R.id.idMoviePicture) ANImageView movieImageView;
    @BindView(R.id.idNoReviewTv) TextView noReviewTV;
    @BindView(R.id.idTextViewNoTrailer) TextView noTrailerTV;
    @BindView(R.id.idRecyclerViewReview) RecyclerView reviewRecyclerView;
    @BindView(R.id.idTrailerRecyclerView) RecyclerView trailerRecyclerView;
    @BindView(R.id.idFloatingActionButton) FloatingActionButton floatingActionButton;

    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    ArrayList<Trailer> trailerArrayList;
    ArrayList<Review> reviewArrayList;
    private static final String YOUTUBE_APP_PACKAGE = "com.google.android.youtube";
    private static final String YOUTUBE_URL_APP = "vnd.youtube://";
    private static final String YOUTUBE_URL_BROWSER = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAMETER = "v";
    static String id;
    String movieTitle, movieReleasedate, movieRating,movieOverview, moviePosterURl, movieId;
    FavoriteMoviesDbHelper favoriteMoviesDbHelper;
    boolean isFavorite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
            getDataFromIntent();
             setViewData();
          id = getIntent().getStringExtra("movieID");
        loadReviewRecycler();
      loadTrailerRecycler();
        floatingActionButton.setOnClickListener(this);
        favoriteMoviesDbHelper = new FavoriteMoviesDbHelper(this);


    }

    public void getDataFromIntent(){
        Intent intent = getIntent();
        if (intent != null){





            movieTitle = intent.getStringExtra("movieTitle");
            movieOverview = intent.getStringExtra("moviewOverview");
            moviePosterURl = intent.getStringExtra("moviePosterUrl");
            movieRating = intent.getStringExtra("movieRating");
            movieReleasedate = intent.getStringExtra("movieReleaseDate");
            movieId = intent.getStringExtra("movieID");

        }
         }

    public String buildImageUrl(String posterURl){
        String BaseURL =  "http://image.tmdb.org/t/p/";
        String imageSize =  "w185";
        StringBuilder urlBulder = new StringBuilder(BaseURL);
        urlBulder.append(imageSize);
        urlBulder.append(posterURl);
        String ImageURL = urlBulder.toString();
        return ImageURL;
    }

    public  void setViewData(){
        releaseDateTV.setText(movieReleasedate);
        titleTv.setText(movieTitle);
        ratingTV.setText(movieRating+"/10");
        overviewTv.setText(movieOverview);
        movieImageView.setImageUrl(buildImageUrl(moviePosterURl));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(movieTitle);
    }

    public void loadTrailerRecycler(){
        String  trailerUrl = getString(R.string.base_url) + getIntent().getStringExtra("movieID") + "/videos";

        AndroidNetworking.get(trailerUrl)
                .addQueryParameter(getString(R.string.api_Parameter), getString(R.string.api_key))
                .setPriority(Priority.HIGH)
                .setTag("tag")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MovieDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                        try {


                            JSONObject resulObj = new JSONObject(response);
                            JSONArray trailerJsonArray = resulObj.getJSONArray("results");
                            trailerArrayList = new ArrayList<Trailer>();
                            for (int i = 0; i < trailerJsonArray.length(); i++) {
                                JSONObject trailerObject = (JSONObject) trailerJsonArray.get(i);
                               String type = trailerObject.getString("type");
                                String site = trailerObject.getString("site");
                                String trail = "Trailer";
                                String you = "YouTube";

                                if (type.matches(trail) &&
                                        site.matches(you)) {
                                    String key = trailerObject.getString("key");
                                    String name = trailerObject.getString("name");
                                   Trailer trailer = new Trailer(key, name);
                                    trailerArrayList.add(trailer);
                                }
                            }
                           if (trailerArrayList != null){

                                trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                               trailerAdapter = new TrailerAdapter(trailerArrayList, getApplicationContext());
                               trailerRecyclerView.setAdapter(trailerAdapter);


                               trailerRecyclerView.setHasFixedSize(true);
                                    trailerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Toast.makeText(MovieDetailActivity.this, "clciked ", Toast.LENGTH_SHORT).show();
                                        PlayMovieTrailer(trailerArrayList.get(position).getKey());
                                        }
                                    }));


                           } else {
                               noTrailerTV.setText(getString(R.string.no_trailer));
                               noTrailerTV.setVisibility(View.VISIBLE);
                           }




                            } catch(JSONException e){
                                e.printStackTrace();

                            }
                        }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();

                    }
                });


    }

    public void loadReviewRecycler(){


        String urlBuilder =  getString(R.string.base_url);
        String  reviewUrl = urlBuilder+ getIntent().getStringExtra("movieID") + "/reviews";
        AndroidNetworking.get(reviewUrl)
                .setPriority(Priority.HIGH)
                .setTag("tag")
                .addQueryParameter(getString(R.string.api_Parameter),getString(R.string.api_key))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray reviewArray = responseObject.getJSONArray("results");
                            reviewArrayList = new ArrayList<Review>();
                            for (int i = 0; i < reviewArray.length(); i ++){
                                JSONObject reviewObject = (JSONObject)reviewArray.get(i);
                                String author = reviewObject.getString("author");
                                String content = reviewObject.getString("content");
                                Review review = new Review(author,content);
                                reviewArrayList.add(review);
                            }

                            if (reviewArrayList!= null) {
                                reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                reviewAdapter = new ReviewAdapter(reviewArrayList, getApplicationContext());
                                reviewRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                                reviewRecyclerView.setAdapter(reviewAdapter);
                                reviewRecyclerView.setHasFixedSize(true);
                            } else {
                                noReviewTV.setText(getString(R.string.no_review));
                                noReviewTV.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();

                    }
                });



    }

    private boolean checkForYouTubeApp() {
        return getPackageManager()
                .getLaunchIntentForPackage(YOUTUBE_APP_PACKAGE) != null;
    }
    private void PlayMovieTrailer(String trailerKey) {
        Intent intent;
        if (checkForYouTubeApp()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_APP + trailerKey));
        } else {
            Uri uri = Uri.parse(YOUTUBE_URL_BROWSER)
                    .buildUpon()
                    .appendQueryParameter(VIDEO_PARAMETER, trailerKey)
                    .build();
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idFloatingActionButton:
                clickFavourite();
                break;

            default:
                break;
        }
    }


    public  void clickFavourite(){
      //if (isMovieFav()){

        //  Toast.makeText(this, "Movie is Favorite already ", Toast.LENGTH_SHORT).show();
      //} else {
          ContentValues contentValues = new ContentValues();

          contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_ID,movieId);
          contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_POSTER_URL, moviePosterURl);
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleasedate);
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RATING, movieRating);

          getContentResolver().insert(FavoriteMoviesContract.FavMovieEntry.CONTENT_URI, contentValues);
          isFavorite = true;
          floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.red));
      //}

    }



    public  boolean isMovieFav(){
        SQLiteDatabase favMovieDb = favoriteMoviesDbHelper.getWritableDatabase();
        String whereClause = FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectArgs = {String.valueOf(id)};
        Cursor cursor = favMovieDb.query(FavoriteMoviesContract.FavMovieEntry.TABLE_NAME,
                null,whereClause,selectArgs, null, null, null);

        if (cursor.getCount() ==0){
            return  false;

        } else {
            cursor.close();
            return true;
        }



    }
}
