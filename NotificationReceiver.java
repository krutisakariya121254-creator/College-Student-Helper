package com.example.studybuddy.ui.examcountdown;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.studybuddy.MainActivity;
import com.example.studybuddy.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "studybuddy_channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        if (title == null) title = "StudyBuddy Reminder";
        if (message == null) message = "You have an upcoming task";

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "StudyBuddy Reminders",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}