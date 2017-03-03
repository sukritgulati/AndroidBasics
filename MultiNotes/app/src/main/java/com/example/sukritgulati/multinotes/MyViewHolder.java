package com.example.sukritgulati.multinotes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sukritgulati on 2/23/17.
 */


public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView Id;
    public TextView title;
    public TextView content;
    public TextView date;
    public MyViewHolder(View itemView) {
        super(itemView);
        Id = (TextView) itemView.findViewById(R.id.IdTextView);
        title = (TextView) itemView.findViewById(R.id.TitleTextView);
        content = (TextView) itemView.findViewById(R.id.ContentTextView);
        date = (TextView) itemView.findViewById(R.id.DateTextView);
    }
}
