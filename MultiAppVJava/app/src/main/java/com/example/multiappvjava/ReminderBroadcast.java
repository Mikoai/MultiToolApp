package com.example.multiappvjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.multiappvjava.entity.Reminder;

public class ReminderBroadcast extends BroadcastReceiver {
    String[] remContent = new String[4];

    @Override
    public void onReceive(Context context, Intent intent) {
        remContent = intent.getStringArrayExtra("reminder");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RemindersChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(remContent[1])
                .setContentText(remContent[3])
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Integer.parseInt(remContent[0]), builder.build());
    }
}
