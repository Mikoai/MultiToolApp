package com.example.multiappvjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateNote extends AppCompatActivity {

    ArrayList<Note> allNotes = new ArrayList<>();
    String currentDate;
    String newTitle;
    String newSubt;
    String newDesc;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        FloatingActionButton addBtn = findViewById(R.id.fabDone);
        FloatingActionButton delBtn = findViewById(R.id.fabDelete);
        TextView noteDate = findViewById(R.id.note_datetime);
        EditText noteTitle = findViewById(R.id.note_title);
        EditText noteSubt = findViewById(R.id.note_subtitle);
        EditText noteDesc = findViewById(R.id.note_description);
        noteId = getIntent().getIntExtra("NoteId", -1);

        //load notes to ArrayList, get current date
        loadNotes();

        //saved note content
        if(noteId > -1) {
            noteDate.setText(allNotes.get(noteId).getDateTime());
            noteTitle.setText(allNotes.get(noteId).getTitle());
            noteSubt.setText(allNotes.get(noteId).getSubtitle());
            noteDesc.setText(allNotes.get(noteId).getDescription());
            //allNotes.remove(noteId);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy   hh:mm");
            currentDate = sdf.format(new Date());
            noteDate.setText(currentDate);
        }

        addBtn.setOnClickListener(v -> {
            if(addNote(noteTitle, noteSubt, noteDesc)){
                //if it is edit, edit note in arrayList, else add a new one
                if(noteId > -1){
                    allNotes.get(noteId).setTitle(newTitle);
                    allNotes.get(noteId).setSubtitle(newSubt);
                    allNotes.get(noteId).setDescription(newDesc);
                } else allNotes.add(new Note(newTitle, currentDate, newSubt, newDesc));
                saveNotes();
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Notes.class);
                startActivity(intent);
                //finish();
            }
        });

        delBtn.setOnClickListener(v -> {
            // if it's edited note, delete
            if(noteId > -1){
                allNotes.remove(noteId);
                saveNotes();
            }
            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
            //finish();
        });

    }

    //I don't even
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setContentView(R.layout.activity_create_note);
//        FloatingActionButton addBtn = findViewById(R.id.fabDone);
//        FloatingActionButton delBtn = findViewById(R.id.fabDelete);
//        TextView noteDate = findViewById(R.id.note_datetime);
//        EditText noteTitle = findViewById(R.id.note_title);
//        EditText noteSubt = findViewById(R.id.note_subtitle);
//        EditText noteDesc = findViewById(R.id.note_description);
//        noteId = getIntent().getIntExtra("NoteId", -1);
//
//        //load notes to ArrayList, get current date
//        loadNotes();
//
//        //saved note content
//        if(noteId > -1) {
//            noteDate.setText(allNotes.get(noteId).getDateTime());
//            noteTitle.setText(allNotes.get(noteId).getTitle());
//            noteSubt.setText(allNotes.get(noteId).getSubtitle());
//            noteDesc.setText(allNotes.get(noteId).getDescription());
//            //allNotes.remove(noteId);
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy   hh:mm");
//            currentDate = sdf.format(new Date());
//            noteDate.setText(currentDate);
//        }
//
//        addBtn.setOnClickListener(v -> {
//            if(addNote(noteTitle, noteSubt, noteDesc)){
//                //if it is edit, edit note in arrayList, else add a new one
//                if(noteId > -1){
//                    allNotes.get(noteId).setTitle(newTitle);
//                    allNotes.get(noteId).setSubtitle(newSubt);
//                    allNotes.get(noteId).setDescription(newDesc);
//                } else allNotes.add(new Note(newTitle, currentDate, newSubt, newDesc));
//                saveNotes();
//                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, Notes.class);
//                startActivity(intent);
//                //finish();
//            }
//        });
//
//        delBtn.setOnClickListener(v -> {
//            // if it's edited note, delete
//            if(noteId > -1){
//                allNotes.remove(noteId);
//                saveNotes();
//            }
//            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, Notes.class);
//            startActivity(intent);
//            //finish();
//        });
//
//    }

    private boolean addNote(EditText noteTitle, EditText noteSubt, EditText noteDesc){
        boolean flag = true;
        if(TextUtils.isEmpty(noteTitle.getText())){
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newTitle = noteTitle.getText().toString();

        if(TextUtils.isEmpty(noteSubt.getText())){
            Toast.makeText(this, "Subtitle cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newSubt = noteSubt.getText().toString();

        if(TextUtils.isEmpty(noteDesc.getText())){
            Toast.makeText(this, "Desctiption cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newDesc = noteDesc.getText().toString();

        return flag;
    }

    private void saveNotes(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedNotes", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allNotes);
        prefsEditor.putString("notes", json);
        prefsEditor.apply();
    }

    private void loadNotes(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        allNotes = gson.fromJson(json, type);
        if (allNotes == null) {
            allNotes = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Notes.class);
        startActivity(intent);
        //finish();
    }
}