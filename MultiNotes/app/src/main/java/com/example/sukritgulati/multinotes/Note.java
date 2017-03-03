package com.example.sukritgulati.multinotes;

import java.io.ObjectInput;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sukritgulati on 2/23/17.
 */

public class Note implements Serializable,Comparable<Note>{
    private static final long serializationUID = 1L;
    String id;
    String title;
    String date;
    String noteText;


    public Note(String id, String title, String date, String noteText) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.noteText = noteText;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public int compareTo(Note o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, h:mm:ss a", Locale.ENGLISH);
        try {
        Date d1 = dateFormat.parse(o.getDate());

            Date d2= dateFormat.parse(getDate());
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
