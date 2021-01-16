package com.example.multiappvjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Contact;
import com.example.multiappvjava.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    static ArrayList<Contact> allContacts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddContact);
        ListView contactsList = findViewById(R.id.contactsList);
        SearchView sv = findViewById(R.id.contactsSearchView);
        loadContacts();

        //displaying notes
        CustomAdapter arrayAdapter = new CustomAdapter(this, allContacts);
        contactsList.setAdapter(arrayAdapter);

        contactsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CreateContact.class);
            intent.putExtra("ContactId", position);
            Toast.makeText(this, "Click", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        });

        fabAddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateContact.class);
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
                System.out.println("Filter");
                return false;
            }
        });
    }

    private void loadContacts(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedContacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contacts", null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        allContacts = gson.fromJson(json, type);
        if (allContacts == null) {
            allContacts = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}