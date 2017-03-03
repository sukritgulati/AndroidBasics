package com.example.sukritgulati.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        title = (EditText) findViewById(R.id.DetailTitleView);
        content = (EditText) findViewById(R.id.DetailContentView);
        content.setMovementMethod(new ScrollingMovementMethod());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusave,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveNoteMenu:
                int saveState = save();
                if(saveState == 1){
                    Intent intent = new Intent();
                    intent.putExtra("NOTE_DETAIL", note);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if(saveState== -1){
                    Intent intent = new Intent();
                    intent.putExtra("NOTE_ERROR", NoteDetailActivity.class.getSimpleName());
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    NoteDetailActivity.super.onBackPressed();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent.hasExtra("NOTE_DETAIL")) {
            note = (Note) intent.getSerializableExtra("NOTE_DETAIL");
            title.setText(note.getTitle());
            title.setSelection(note.getTitle().toString().length());
            content.setText(note.getNoteText());
        }else{
            note = new Note(getDateTime(),"","","");
        }
    }

    @Override
    public void onBackPressed() {
            int saveState = save();
        if(saveState == 1) {
            final TextView textView = new TextView(this);
            textView.setText(getString(R.string.dialog_string) + " '" + title.getText().toString() + "' ?");
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 10, 0, 0);
            textView.setTextSize(20);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(textView);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent();
                    intent.putExtra("NOTE_DETAIL", note);
                    setResult(RESULT_OK, intent);
                    NoteDetailActivity.super.onBackPressed();

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NoteDetailActivity.super.onBackPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(saveState == -1){
            Intent intent = new Intent();
            intent.putExtra("NOTE_ERROR", NoteDetailActivity.class.getSimpleName());
            setResult(RESULT_OK, intent);
            NoteDetailActivity.super.onBackPressed();
        }else{
            NoteDetailActivity.super.onBackPressed();
        }

    }

    private int save(){
        String checkTitle = title.getText().toString();
        if(checkTitle.isEmpty() && content.getText().toString().isEmpty()){
            return 0;
        }
        if(checkTitle != null && !checkTitle.isEmpty() && checkTitle.trim().length() != 0){
            if(!note.getTitle().equals(title.getText().toString())
                    || !note.getNoteText().equals(content.getText().toString())){
                note.setTitle(title.getText().toString());
                note.setNoteText(content.getText().toString());
                note.setDate(getDateTime());
                return 1;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
    }
    private String getDateTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, h:mm:ss a", Locale.ENGLISH);

        return dateFormat.format(date);
    }
}
