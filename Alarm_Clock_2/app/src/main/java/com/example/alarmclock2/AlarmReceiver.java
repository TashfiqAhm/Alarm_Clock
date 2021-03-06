package com.example.alarmclock2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;




public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager nManagwer;
    int notification_id;
    @Override
    public void onReceive(Context context, Intent intent) {

        notification_id = intent.getIntExtra("notification_id",0);
        String title = intent.getStringExtra("title");

        Intent mainIntent = new Intent(context,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,notification_id,mainIntent,0);

       nManagwer = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("Reminder")
                .setContentText("title")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        nManagwer.notify(notification_id,builder.build());

    }
}
