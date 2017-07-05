package com.jp.popularmoviess1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.widget.ANImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENIOVO on 5/24/2017.
 */

public class MovieAdapt  extends BaseAdapter {
   ArrayList<Movie> movieList;
    Context context;
    private final static String BASE_POSTER_URL="https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185" ;
    public MovieAdapt(ArrayList<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Movie myMovie = movieList.get(position);
        ANImageView movieImageView;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            movieImageView = (ANImageView)inflater.inflate(R.layout.movie_front_page_layout,parent,false);
        }

        else {
            movieImageView = (ANImageView) convertView;
        }
        String url = new StringBuilder()
                .append(BASE_POSTER_URL)
                .append(IMAGE_SIZE)
                .append(myMovie.getPosterUrl().trim())
                .toString();
        movieImageView.setImageUrl(url);
        movieImageView.setErrorImageResId(R.mipmap.ic_launcher_round);
        movieImageView.setDefaultImageResId(R.mipmap.ic_launcher);

        return movieImageView;
    }

    public void cleanUp(){
        if (movieList.size() > 0){
            movieList.clear();
        }
    }
}
