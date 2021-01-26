package com.example.multiappvjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button notesBtn;
    private Button reminderBtn;
    private Button contactBtn;
    private Button mediaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesBtn = findViewById(R.id.button);
        reminderBtn = findViewById(R.id.button2);
        contactBtn = findViewById(R.id.button3);
        mediaBtn = findViewById(R.id.button4);

        notesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
        });

        reminderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Reminders.class);
            startActivity(intent);
        });

        contactBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Contacts.class);
            startActivity(intent);
        });

        mediaBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MediaCenter.class);
            startActivity(intent);
        });

    }
}