package com.example.multiappvjava.entity;

import android.net.Uri;

import java.io.File;

public class Contact {
    String image;
    String name;
    String number;
    String email;
    boolean male;

    public Contact(String img, String name, String number, String email, boolean male) {
        this.image = img;
        this.name = name;
        this.number = number;
        this.email = email;
        this.male = male;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    @Override
    public String toString() {
        return image + "\n" + name + "\n" + number;
    }
}
