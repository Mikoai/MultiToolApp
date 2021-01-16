package com.example.multiappvjava;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateReminder extends AppCompatActivity {

    ArrayList<Reminder> allReminders = new ArrayList<>();
    String newDateTime;
    String newTitle;
    String newDesc;
    int reminderId;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        FloatingActionButton addBtn = findViewById(R.id.fabDone);
        FloatingActionButton delBtn = findViewById(R.id.fabDelete);
        TextView reminderDateTime = findViewById(R.id.reminder_datetime);
        EditText reminderTitle = findViewById(R.id.reminder_title);
        EditText reminderDesc = findViewById(R.id.reminder_description);
        reminderId = getIntent().getIntExtra("ReminderId", -1);

        //load reminders to ArrayList, get current date
        loadReminders();

        //saved reminder content
        if(reminderId > -1) {
            reminderDateTime.setText(allReminders.get(reminderId).getDateTime());
            reminderTitle.setText(allReminders.get(reminderId).getTitle());
            reminderDesc.setText(allReminders.get(reminderId).getDescription());
            //allReminders.remove(reminderId);
        } else {
            newDateTime = sdf.format(new Date());
            reminderDateTime.setText(newDateTime);
        }

        reminderDateTime.setOnClickListener(v ->  {
            showDateTimeDialog(reminderDateTime);
        });

        addBtn.setOnClickListener(v -> {
            if(addReminder(reminderTitle, reminderDateTime, reminderDesc)){
                Intent intentBr = new Intent(this, ReminderBroadcast.class);
                String[] reminderContent = new String[4];
                //if it is edit, edit reminder in arrayList, else add a new one
                if(reminderId > -1){
                    allReminders.get(reminderId).setTitle(newTitle);
                    allReminders.get(reminderId).setDateTime(newDateTime);
                    allReminders.get(reminderId).setDescription(newDesc);

                    calendar.set(Calendar.YEAR, Integer.parseInt(allReminders.get(reminderId).getDateTime().substring(0, 4)));
                    calendar.set(Calendar.MONTH, Integer.parseInt(allReminders.get(reminderId).getDateTime().substring(5, 7)) - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(allReminders.get(reminderId).getDateTime().substring(8, 10)));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(allReminders.get(reminderId).getDateTime().substring(11, 13)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(allReminders.get(reminderId).getDateTime().substring(14)));

//                    try {
//                        Date date = sdf.parse(allReminders.get(reminderId).getDateTime());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                    reminderContent[0] = String.valueOf(allReminders.get(reminderId).getId());
                    reminderContent[1] = allReminders.get(reminderId).getTitle();
                    reminderContent[2] = allReminders.get(reminderId).getDateTime();
                    reminderContent[3] = allReminders.get(reminderId).getDescription();
                    intentBr.putExtra("reminder", reminderContent);
                } else{
                    allReminders.add(new Reminder(newTitle, newDateTime, newDesc));
                    reminderContent[0] = String.valueOf(allReminders.get(allReminders.size()-1).getId());
                    reminderContent[1] = allReminders.get(allReminders.size()-1).getTitle();
                    reminderContent[2] = allReminders.get(allReminders.size()-1).getDateTime();
                    reminderContent[3] = allReminders.get(allReminders.size()-1).getDescription();
                    intentBr.putExtra("reminder", reminderContent);
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(reminderContent[0]), intentBr, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 20000, pendingIntent);

                saveReminders();
                Toast.makeText(this, "Reminder saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Reminders.class);
                startActivity(intent);
                //finish();
            }
        });

        delBtn.setOnClickListener(v -> {
            // if it's edited reminder, delete
            if(reminderId > -1){
                Intent intentBr = new Intent(this, ReminderBroadcast.class);
                String[] reminderContent = new String[4];
                reminderContent[0] = String.valueOf(allReminders.get(reminderId).getId());
                reminderContent[1] = allReminders.get(reminderId).getTitle();
                reminderContent[2] = allReminders.get(reminderId).getDateTime();
                reminderContent[3] = allReminders.get(reminderId).getDescription();
                intentBr.putExtra("reminder", reminderContent);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(reminderContent[0]), intentBr, PendingIntent.FLAG_NO_CREATE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                allReminders.remove(reminderId);
                saveReminders();
            }
            Toast.makeText(this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Reminders.class);
            startActivity(intent);
            //finish();
        });

    }


    //I don't even
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setContentView(R.layout.activity_create_reminder);
//        FloatingActionButton addBtn = findViewById(R.id.fabDone);
//        FloatingActionButton delBtn = findViewById(R.id.fabDelete);
//        TextView reminderDate = findViewById(R.id.reminder_datetime);
//        EditText reminderTitle = findViewById(R.id.reminder_title);
//        EditText reminderSubt = findViewById(R.id.reminder_subtitle);
//        EditText reminderDesc = findViewById(R.id.reminder_description);
//        reminderId = getIntent().getIntExtra("ReminderId", -1);
//
//        //load reminders to ArrayList, get current date
//        loadReminders();
//
//        //saved reminder content
//        if(reminderId > -1) {
//            reminderDate.setText(allReminders.get(reminderId).getDateTime());
//            reminderTitle.setText(allReminders.get(reminderId).getTitle());
//            reminderSubt.setText(allReminders.get(reminderId).getSubtitle());
//            reminderDesc.setText(allReminders.get(reminderId).getDescription());
//            //allReminders.remove(reminderId);
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy   hh:mm");
//            currentDate = sdf.format(new Date());
//            reminderDate.setText(currentDate);
//        }
//
//        addBtn.setOnClickListener(v -> {
//            if(addReminder(reminderTitle, reminderSubt, reminderDesc)){
//                //if it is edit, edit reminder in arrayList, else add a new one
//                if(reminderId > -1){
//                    allReminders.get(reminderId).setTitle(newTitle);
//                    allReminders.get(reminderId).setSubtitle(newSubt);
//                    allReminders.get(reminderId).setDescription(newDesc);
//                } else allReminders.add(new Reminder(newTitle, currentDate, newSubt, newDesc));
//                saveReminders();
//                Toast.makeText(this, "Reminder saved!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, Reminders.class);
//                startActivity(intent);
//                //finish();
//            }
//        });
//
//        delBtn.setOnClickListener(v -> {
//            // if it's edited reminder, delete
//            if(reminderId > -1){
//                allReminders.remove(reminderId);
//                saveReminders();
//            }
//            Toast.makeText(this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, Reminders.class);
//            startActivity(intent);
//            //finish();
//        });
//
//    }

    private void showDateTimeDialog(TextView reminderDateTime) {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                reminderDateTime.setText(sdf.format(calendar.getTime()));
            };

            new TimePickerDialog(CreateReminder.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private boolean addReminder(EditText reminderTitle,TextView reminderDateTime, EditText reminderDesc){
        boolean flag = true;
        if(TextUtils.isEmpty(reminderTitle.getText())){
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newTitle = reminderTitle.getText().toString();

        if(TextUtils.isEmpty(reminderDateTime.getText())){
            Toast.makeText(this, "Date and time cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newDateTime = reminderDateTime.getText().toString();

        if(TextUtils.isEmpty(reminderDesc.getText())){
            Toast.makeText(this, "Desctiption cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else newDesc = reminderDesc.getText().toString();

        return flag;
    }

    private void saveReminders(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedReminders", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allReminders);
        prefsEditor.putString("reminders", json);
        prefsEditor.apply();
    }

    private void loadReminders(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedReminders", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>() {
        }.getType();
        allReminders = gson.fromJson(json, type);
        if (allReminders == null) {
            allReminders = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Reminders.class);
        startActivity(intent);
        //finish();
    }
}