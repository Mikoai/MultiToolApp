package com.example.multiappvjava.entity;

import java.util.Random;

public class Reminder {
    int id;
    String title;
    String dateTime;
    String description;
    Random random = new Random();

    public Reminder(String title, String dateTime, String description){
        this.id = random.nextInt(2147483647);
        this.title = title;
        this.dateTime = dateTime;
        this.description = description;
    }

    public long getId() { return id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title + "\n" + dateTime;
    }
}
