package com.example.multiappvjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.multiappvjava.entity.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotesDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Intent intent;
    private ArrayList<Note> notesArray = new ArrayList<>();

    public NotesDataProvider(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        loadNotes();
        //System.out.println("ArayList size: "+notesArray.size());
    }

    @Override
    public void onDataSetChanged() {
        loadNotes();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return notesArray.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, notesArray.get(position).getTitle());
        Intent intent = new Intent(context, CreateNote.class);
        intent.putExtra("NoteId", position);
        view.setOnClickFillInIntent(android.R.id.text1, intent);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadNotes(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("savedNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        notesArray = gson.fromJson(json, type);
        if (notesArray == null) {
            notesArray = new ArrayList<>();
        }
        //this.notesArray = Notes.notesArray;
    }
}
