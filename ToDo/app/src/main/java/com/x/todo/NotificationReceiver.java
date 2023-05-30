package com.x.todo;

import android.Manifest;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("id")) {
            final Task[] task = new Task[1];

            Thread dbThread = new Thread(() -> {
                task[0] = TaskDatabase.getInstance(context).taskDao().findTaskById(intent.getIntExtra("id", 0));
            });

            dbThread.start();

            try {
                dbThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Intent resultIntent = new Intent(context, TaskActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.putExtra("id", task[0].getId());

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "id")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle(task[0].getTitle())
                    .setContentText(task[0].getDescription())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(task[0].getId(), notificationBuilder.build());
            }
        }
    }
}
