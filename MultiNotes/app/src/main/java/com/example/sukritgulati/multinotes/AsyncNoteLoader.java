package com.example.sukritgulati.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sukritgulati on 2/25/17.
 */

public class AsyncNoteLoader extends AsyncTask<ArrayList<Note>, Integer, ArrayList<Note>>{

    private NoteSummaryActivity mainActivity;
    private int count;

    public AsyncNoteLoader(NoteSummaryActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected ArrayList<Note> doInBackground(ArrayList<Note>... params) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
           noteList =  parseJSON(sb.toString());


        } catch (FileNotFoundException e) {
            return  noteList;
            //Toast.makeText(this, mainActivity.getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteList;
    }

    private ArrayList<Note> parseJSON(String s) {

        ArrayList<Note> noteList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jNotes = (JSONObject) jObjMain.get(i);
                String id = jNotes.getString("id");
                String title = jNotes.getString("title");
                String time = jNotes.getString("time");
                String content = jNotes.getString("content");

                noteList.add(new Note(id,title,time,content));
            }
            return noteList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(ArrayList<Note> notes) {
       mainActivity.updateData(notes);
    }
}
