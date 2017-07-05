package com.jp.popularmoviess1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.widget.ANImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENIOVO on 5/22/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movieList;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.movie_front_page_layout, parent,false);
          ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    Movie movie = movieList.get(position);

         holder.movieImageView.setImageUrl(buildImageUrl(movie.getPosterUrl()));
       holder.movieImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.movieImageView.setErrorImageResId(R.mipmap.ic_launcher_round);

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         public ANImageView movieImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ANImageView)itemView.findViewById(R.id.id_movieImage);

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




}
