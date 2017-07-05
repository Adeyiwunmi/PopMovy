package com.jp.popularmoviess1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.method.MovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Movie> movieArrayList;
    GridView gridView;
    ProgressDialog progressDialog;
    MovieAdapt movieAdapt;
    SharedPreferences sharedPreferences;
    final static String PREF_NAME = "pref_name", MOVIE_LIST= "movie_list"
            ,KEY_SELECTED_POSITION = "selected_position";
    SharedPreferences.Editor editor;
    FavoriteMoviesDbHelper dbHelper;
    static int gridViewPosition;
    Parcelable state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gridView = (GridView) findViewById(R.id.idMovieGrid);
        dbHelper = new FavoriteMoviesDbHelper(this);

        progressDialog = new ProgressDialog(MainActivity.this);
        Toast.makeText(this, String.valueOf(gridViewPosition), Toast.LENGTH_SHORT).show();

        progressDialog.setMessage(getString(R.string.progressDialogMessage));

        if (savedInstanceState ==null ||!savedInstanceState.containsKey(MOVIE_LIST)){
            movieArrayList = new ArrayList<>();
          showProgressDialog();
            reQuestBasedOnsort();
            setGridPosition();
        } else {

            movieArrayList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);

          movieAdapt = new MovieAdapt(movieArrayList, getApplicationContext());
            gridView.setAdapter(movieAdapt);
            setGridPosition();

//            if (state!=null){
  //              gridView.onRestoreInstanceState(state);
    //        }

      //      gridViewPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
        //    gridView.setSelection(gridViewPosition);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              gridViewPosition = position;
                Movie movieSend = movieArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movieSend);
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity
                        .class);
                intent.putExtra("movieTitle", movieSend.getTitle());
                intent.putExtra("moviePosterUrl", movieSend.getPosterUrl());
                intent.putExtra("movieRating", movieSend.getUserRatiing());
                intent.putExtra("moviewOverview", movieSend.getOverView());
                intent.putExtra("movieReleaseDate", movieSend.getReleaseDate());
                intent.putExtra("movieID", movieSend.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });




    }

    @Override
    protected void onStart() {
     setGridPosition();
        super.onStart();
    }


    @Override
    protected void onResume(){
      //  gridView.setSelection(gridViewPosition);
        setGridPosition();
        super.onResume();
    }


    @Override
    protected void onPause() {
       gridViewPosition = gridView.getFirstVisiblePosition();
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setGridPosition();
        //gridViewPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
        //gridView.setSelection(gridViewPosition);

       // if (state!= null){
         //   gridView.onRestoreInstanceState(state);

        //}
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_most_popular) {

            changeSort(getString(R.string.pref_most_popular));
            makeRequestSuccess("popular/");
            //do for action most popular
            return true;
        } else if (id == R.id.action_top_rating) {

            changeSort(getString(R.string.pref_rating_label));
            makeRequestSuccess("top_rated/");

            return true;
        } else if (id == R.id.menu_refresh) {
            reQuestBasedOnsort();
            return true;
        }
        else  if (id == R.id.action_favorites){
            changeSort("favorites");
            loadFavorites();

        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }


    public void hideProgressDialog() {

        progressDialog.hide();
    }


    public void makeRequestSuccess(String prefString) {

        StringBuilder urlBuilder = new StringBuilder(getString(R.string.base_url));

        urlBuilder.append(prefString);

        String url = urlBuilder.toString();
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .addQueryParameter(getString(R.string.api_Parameter), getString(R.string.api_key))
                .setTag("Tag")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            movieArrayList = new ArrayList<Movie>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movieJsonObject = (JSONObject) jsonArray.get(i);
                                String movieTitle = movieJsonObject.getString("title");
                                String moviePosterUrl = movieJsonObject.getString("poster_path");
                                String movieOverview = movieJsonObject.getString("overview");
                                String movieRating = movieJsonObject.getString("vote_average");
                                String movieReleaseDate = movieJsonObject.getString("release_date");
                               String movieId = movieJsonObject.getString("id");
                                Movie movie = new Movie(movieTitle,
                                        moviePosterUrl,
                                        movieOverview,
                                        movieRating,
                                        movieReleaseDate, movieId);
                                movieArrayList.add(movie);
                            }
                            movieAdapt = new MovieAdapt(movieArrayList, getApplicationContext());
                            gridView.setAdapter(movieAdapt);
                          setGridPosition();
                            hideProgressDialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        hideProgressDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(getString(R.string.alertMessage_noNetwork));
                        builder.setCancelable(false);
                        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reQuestBasedOnsort();
                            }
                        });
                        builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("No Network connection");
                        dialog.show();
                    }
                });


    }

    public void changeSort(String newSort) {
        editor = sharedPreferences.edit();
        editor.putString("key", newSort);
        editor.commit();

    }

    public void reQuestBasedOnsort() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String select = sharedPreferences.getString("key", " ");
          if (select.matches("favorites")){
              loadFavorites();
              getSupportActionBar().setTitle(getString(R.string.favorites));

          } else {

              switch (select) {
                  case "Most Popular":
                      makeRequestSuccess("popular/");
                   getSupportActionBar().setTitle(getString(R.string.pref_most_popular));
                      break;

                  case "Top Rating":
                      makeRequestSuccess("top_rated/");
                      getSupportActionBar().setTitle(getString(R.string.pref_rating_label));
                      break;

                  default:
                      makeRequestSuccess("popular/");
                      break;
              }

          }
    }

    @Override
    public void onSaveInstanceState (Bundle  outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieArrayList);
        gridViewPosition = gridView.getFirstVisiblePosition();
        outState.putInt(KEY_SELECTED_POSITION, gridViewPosition);
         state = gridView.onSaveInstanceState();
        outState.putParcelable("key", state);


        super.onSaveInstanceState(outState);

    }
       public void loadFavorites(){
           hideProgressDialog();
           Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavMovieEntry.CONTENT_URI,
                   null,null, null, null);
           movieArrayList = new ArrayList<Movie>();
           assert  cursor != null;

           if (cursor != null){

               if (cursor.getCount()> 0){
                   for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                       Movie movie = new Movie(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_TITLE)),
                               cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_POSTER_URL)),
                               cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_OVERVIEW)),
                               cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RATING)),
                               cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_RELEASE_DATE)),
                               cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.COLUMN_MOVIE_ID)));

                       movieArrayList.add(movie);
                   }
                   gridView.setAdapter(new MovieAdapt(movieArrayList, getApplicationContext()));
                     setGridPosition();

               } else {

                   Toast.makeText(this, getString(R.string.no_favorites), Toast.LENGTH_SHORT).show();
               }
               cursor.close();
           }   else {
               Toast.makeText(this, "cursor is null", Toast.LENGTH_SHORT).show();
           }



       }


public void setGridPosition(){
      //  gridView = (GridView)findViewById(R.id.idMovieGrid);
      gridView.smoothScrollToPosition(gridViewPosition);
    gridView.setSelection(gridViewPosition);
}

}





