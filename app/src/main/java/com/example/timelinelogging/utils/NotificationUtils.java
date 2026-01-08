package com.example.timelinelogging.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtils {

    public static final String CHANNEL_ID = "journal_channel";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Journaling Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

