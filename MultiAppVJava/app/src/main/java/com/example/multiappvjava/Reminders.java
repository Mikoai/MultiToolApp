package com.example.multiappvjava;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Note;
import com.example.multiappvjava.entity.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class Reminders extends AppCompatActivity {
    static ArrayList<Note> remindersArray = new ArrayList<>();
    ArrayAdapter<Note> arrayAdapter;
    public static final String channelId = "RemindersChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddReminder);
        ListView remindersList = findViewById(R.id.remindersList);
        SearchView sv = findViewById(R.id.reminderSearchView);
        loadNotes();

        createNotifChannel();

        //displaying reminders
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersArray);
        remindersList.setAdapter(arrayAdapter);
        //waiting for click to edit reminder
        remindersList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CreateReminder.class);
            intent.putExtra("ReminderId", position);
            startActivity(intent);
            finish();
        });

        //Add note fab
        fabAddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateReminder.class);
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

            // Dynamic search -- searches while typing
            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void createNotifChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(channelId, "RemindersChannel", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("High prio reminders channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    private void loadNotes(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedReminders", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>() {
        }.getType();
        remindersArray = gson.fromJson(json, type);
        if (remindersArray == null) {
            remindersArray = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}