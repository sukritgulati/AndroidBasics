package com.example.sukritgulati.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteSummaryActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private int NEW = 1;
    private int EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_summary);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        noteAdapter = new NoteAdapter(noteList,this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new AsyncNoteLoader(this).execute();
//        for (int i = 0; i < 20; i++) {
//            noteList.add(new Note(String.valueOf(i),"title"+i,"date","The Ligue 1 giants’ chief was quoted in the French press as saying he expected very significant sales before June 30, which seems likely to include one of their most valuable players in Lacazette. Read more: http://metro.co.uk/2017/02/24/arsenal-get-alexandre-lacazette-transfer-boost-as-lyon-chief-expects-significant-sales-6471364/#ixzz4ZdvOQ3I6"));
//        }
        Collections.sort(noteList);
    }
    public void updateData(ArrayList<Note> nList) {
        noteList.addAll(nList);
        Collections.sort(noteList);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        if(noteList.size()!=0) {
            Collections.sort(noteList);
            noteAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.newNoteMenu:
                Intent intent = new Intent(NoteSummaryActivity.this,NoteDetailActivity.class);
                //intent.putExtra("NOTE_DETAIL",note);
                startActivityForResult(intent,NEW);
                return true;
            case R.id.infoMenu:
                Intent in = new Intent(NoteSummaryActivity.this, InfoActivity.class);
                in.putExtra(Intent.EXTRA_TEXT, NoteSummaryActivity.class.getSimpleName());
                startActivity(in);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        Note note = noteList.get(pos);
        Intent intent = new Intent(NoteSummaryActivity.this,NoteDetailActivity.class);
        intent.putExtra("NOTE_DETAIL",note);
        startActivityForResult(intent,EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT){
            if(resultCode == RESULT_OK){
                if(data.hasExtra("NOTE_ERROR")){
                    Toast.makeText(this,"un-titled Activity was not saved" , Toast.LENGTH_LONG).show();
                }else {
                    Note newNote = (Note) data.getSerializableExtra("NOTE_DETAIL");
                    int i = 0;
                    for (i = 0; i < noteList.size(); i++) {
                        if (noteList.get(i).getId().equals(newNote.getId())) {
                            noteList.get(i).setTitle(newNote.getTitle());
                            noteList.get(i).setDate(newNote.getDate());
                            noteList.get(i).setNoteText(newNote.getNoteText());
                            break;
                        }
                    }
                }
                Collections.sort(noteList);
                noteAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == NEW){
            if(resultCode == RESULT_OK){
                if(data.hasExtra("NOTE_ERROR")){
                    Toast.makeText(this,"un-titled Activity was not saved" , Toast.LENGTH_LONG).show();
                }else {
                    Note newNote = (Note) data.getSerializableExtra("NOTE_DETAIL");
                    noteList.add(newNote);
                    Collections.sort(noteList);
                    noteAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveJson();

    }

    public void saveJson(){
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writeNoteArray(writer,noteList);
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeNoteArray(JsonWriter writer, List<Note> noteList){
        try {
            writer.beginArray();
            for (Note note : noteList) {
                writeNote(writer,note);
            }
            writer.endArray();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeNote(JsonWriter writer, Note note){
        try {
            writer.beginObject();
            writer.name("id").value(note.getId());
            writer.name("title").value(note.getTitle());
            writer.name("time").value(note.getDate());
            writer.name("content").value(note.getNoteText());
            writer.endObject();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
       final int pos = recyclerView.getChildLayoutPosition(v);

        final TextView textView = new TextView(this);
        textView.setText(getString(R.string.delete_string) +" '"+ noteList.get(pos).getTitle().toString() + "' ?");
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,20,0,0);
        textView.setTextSize(20);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textView);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteList.remove(pos);
                Collections.sort(noteList);
                noteAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }
}
