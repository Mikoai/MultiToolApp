package com.example.multiappvjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappvjava.entity.Contact;
import com.example.multiappvjava.entity.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateContact extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    ImageView avatar;
    String customImage = null;
    ArrayList<Contact> contactsArray = new ArrayList<>();
    int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        FloatingActionButton addBtn = findViewById(R.id.fabDone);
        FloatingActionButton delBtn = findViewById(R.id.fabDelete);
        avatar = findViewById(R.id.contactAvatar);
        EditText contactName = findViewById(R.id.contact_name);
        EditText contactNr = findViewById(R.id.contact_Nr);
        EditText contactEmail = findViewById(R.id.contact_email);
        RadioButton male = findViewById(R.id.radioMale);
        RadioButton female = findViewById(R.id.radioFemale);
        contactId = getIntent().getIntExtra("ContactId", -1);
        loadContacts();

        //saved contact content
        if(contactId > -1) {
            customImage = contactsArray.get(contactId).getImage();
            if(customImage == null){
                if(contactsArray.get(contactId).isMale()){
                    avatar.setImageResource(R.drawable.ic_man);
                    male.setChecked(true);
                } else{
                    avatar.setImageResource(R.drawable.ic_woman);
                    female.setChecked(true);
                }
            } else {
                byte[] decodedString = Base64.decode(contactsArray.get(contactId).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatar.setImageBitmap(decodedByte);
                if (contactsArray.get(contactId).isMale())  male.setChecked(true);
                else female.setChecked(true);
            }

            contactName.setText(contactsArray.get(contactId).getName());
            contactNr.setText(contactsArray.get(contactId).getNumber());
            contactEmail.setText(contactsArray.get(contactId).getEmail());

            //allNotes.remove(noteId);
        } else {
            male.setChecked(true);
        }

        avatar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (CreateContact.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //if permission not granted, request it
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    CreateContact.this.requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    CreateContact.this.pickImageFromGallery();
                }
            } else {
                //if OS < marshmallow
                CreateContact.this.pickImageFromGallery();
            }
        });

        addBtn.setOnClickListener(v -> {
            if(addContact(contactName, contactNr)){
                if(contactId > -1) {
                    contactsArray.get(contactId).setImage(customImage);
                    contactsArray.get(contactId).setName(contactName.getText().toString());
                    contactsArray.get(contactId).setNumber(contactNr.getText().toString());
                    contactsArray.get(contactId).setEmail(contactEmail.getText().toString());
                    contactsArray.get(contactId).setMale(male.isChecked());
                    //allNotes.remove(noteId);
                } else contactsArray.add(new Contact(customImage, contactName.getText().toString(), contactNr.getText().toString(), contactEmail.getText().toString(), male.isChecked()));
                saveContacts();
                Toast.makeText(this, "Contact saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Contacts.class);
                startActivity(intent);
            }
        });

        delBtn.setOnClickListener(v -> {
            // if it's edited contact, delete
            if(contactId > -1){
                contactsArray.remove(contactId);
                saveContacts();
            }
            Toast.makeText(this, "Contact deleted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Contacts.class);
            startActivity(intent);
        });

    }

    private boolean addContact(EditText name, EditText number){
        boolean flag = true;
        if(TextUtils.isEmpty(name.getText())){
            Toast.makeText(this, "Contact name cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(TextUtils.isEmpty(number.getText())){
            Toast.makeText(this, "Contact number cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    private void saveContacts(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedContacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactsArray);
        prefsEditor.putString("contacts", json);
        prefsEditor.apply();
//        prefsEditor.clear();
//        prefsEditor.apply();
    }

    private void loadContacts(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedContacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contacts", null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        contactsArray = gson.fromJson(json, type);
        if (contactsArray == null) {
            contactsArray = new ArrayList<>();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                } else{
                    Toast.makeText(CreateContact.this, "Permission denied.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //handle picked image
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //set image to ImageView TODO: zapis dawida sposobem
            avatar.setImageURI(data.getData());
            BitmapDrawable bitmapDrawable = (BitmapDrawable) avatar.getDrawable();
            Bitmap image = bitmapDrawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            customImage = Base64.encodeToString(b, Base64.DEFAULT);
//            System.out.println(customImage);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);
        finish();
    }
}