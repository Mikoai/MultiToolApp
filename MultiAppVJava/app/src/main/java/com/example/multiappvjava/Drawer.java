package com.example.multiappvjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;

public class Drawer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Button sizeBtn5 = findViewById(R.id.sizeBtn1);
        Button sizeBtn10 = findViewById(R.id.sizeBtn2);
        Button sizeBtn15 = findViewById(R.id.sizeBtn3);
        Button sizeBtn25 = findViewById(R.id.sizeBtn4);
        Button sizeBtn40 = findViewById(R.id.sizeBtn5);

        Button colorBtn1 = findViewById(R.id.clrBtn1);
        Button colorBtn2 = findViewById(R.id.clrBtn2);
        Button colorBtn3 = findViewById(R.id.clrBtn3);
        Button colorBtn4 = findViewById(R.id.clrBtn4);
        Button colorBtn5 = findViewById(R.id.clrBtn5);


        sizeBtn5.setOnClickListener(v -> {
            PaintView.setWidth(5f);
        });

        sizeBtn10.setOnClickListener(v -> {
            PaintView.setWidth(10f);
        });

        sizeBtn15.setOnClickListener(v -> {
             PaintView.setWidth(15f);
        });

        sizeBtn25.setOnClickListener(v -> {
            PaintView.setWidth(25f);
        });

        sizeBtn40.setOnClickListener(v -> {
            PaintView.setWidth(40f);
        });


        colorBtn1.setOnClickListener(v -> {
            PaintView.setColor(Color.BLACK);
        });

        colorBtn2.setOnClickListener(v -> {
            PaintView.setColor(Color.RED);
        });

        colorBtn3.setOnClickListener(v -> {
            PaintView.setColor(Color.GREEN);
        });

        colorBtn4.setOnClickListener(v -> {
            PaintView.setColor(Color.BLUE);
        });

        colorBtn5.setOnClickListener(v -> {
            PaintView.setColor(Color.WHITE);
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MediaCenter.class);
        startActivity(intent);
        finish();
    }
}