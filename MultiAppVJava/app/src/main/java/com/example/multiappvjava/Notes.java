package com.example.multiappvjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Notes extends AppCompatActivity {
    static ArrayList<Note> notesArray = new ArrayList<>();
    ArrayAdapter<Note> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddNote);
        ListView notesList = findViewById(R.id.notesList);
        SearchView sv = findViewById(R.id.searchView);
        loadNotes();

        //displaying notes
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesArray);
        notesList.setAdapter(arrayAdapter);
        //waiting for click to edit note
        notesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CreateNote.class);
            intent.putExtra("NoteId", position);
            startActivity(intent);
            finish();
        });

        //Add note fab
        fabAddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateNote.class);
            startActivity(intent);
            finish();
        });

        // Method responsible for search view
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Static search -- searches after click
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if (notes.contains(query)) {
//                    arrayAdapter.getFilter().filter(query);
//                    arrayAdapter.notifyDataSetChanged();
//                }
//                else {
//                    toast("Match not found!");
//                }
                return false;
            }

            // Dynamic search
            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadNotes(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        notesArray = gson.fromJson(json, type);
        if (notesArray == null) {
            notesArray = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
