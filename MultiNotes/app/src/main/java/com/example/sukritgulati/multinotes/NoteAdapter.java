package com.example.sukritgulati.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import java.util.List;

/**
 * Created by sukritgulati on 2/23/17.
 */

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Note> noteList;
    private NoteSummaryActivity ma;
    public NoteAdapter(List<Note> noteList, NoteSummaryActivity ma) {
        this.noteList = noteList;
        this.ma = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        myView.setOnClickListener(ma);
        myView.setOnLongClickListener(ma);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.Id.setText(note.getId());
        holder.date.setText(note.getDate());
        holder.title.setText(note.getTitle());
        String YourString = note.getNoteText();
        if(YourString.length()>80){
            YourString  =  YourString.substring(0,79)+"...";
        }
        holder.content.setText(YourString);

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
