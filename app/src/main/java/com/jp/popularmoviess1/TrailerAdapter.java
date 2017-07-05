package com.jp.popularmoviess1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LENIOVO on 7/1/2017.
 */

    public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
      ArrayList<Trailer> trailerList;
     Context context;


    public TrailerAdapter(ArrayList<Trailer> trailerList, Context context) {
        this.trailerList = trailerList;
        this.context = context;

    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.trailer_item_layout,parent,false );
        ViewHolder viewHolder = new ViewHolder(contactView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
      Trailer trailer = trailerList.get(position);
       holder.trailerNameTv.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView trailerNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerNameTv = (TextView)itemView.findViewById(R.id.idTrailerNameTv);

        }


      }



}
