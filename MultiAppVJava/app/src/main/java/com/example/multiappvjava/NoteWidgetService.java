package com.example.multiappvjava;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NoteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotesDataProvider(this, intent);
    }
}
