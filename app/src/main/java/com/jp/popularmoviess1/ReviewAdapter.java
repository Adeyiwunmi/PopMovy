package com.jp.popularmoviess1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LENIOVO on 7/1/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
   ArrayList<Review> reviewArrayList ;
     Context context;

    public ReviewAdapter(ArrayList<Review> reviewArrayList, Context context) {
        this.reviewArrayList = reviewArrayList;
        this.context = context;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.review_item,parent,false );
        ViewHolder viewHolder = new ViewHolder(contactView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);
        holder.reviewContentTv.setText(review.getContent());
        holder.reviewAuthorTv.setText("By: "+review.getAuthor());

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public TextView reviewAuthorTv,reviewContentTv;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTv = (TextView)itemView.findViewById(R.id.idReviewAuthorText);
            reviewContentTv = (TextView)itemView.findViewById(R.id.idReviewContentText);

        }
    }
}
