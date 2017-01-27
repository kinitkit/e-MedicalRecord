package com.example.kinit.e_medicalrecord.General.Classes.Notification;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.example.kinit.e_medicalrecord.R;

public class Custom_Notification {
    NotificationCompat.Builder builder;
    Notification notification;
    Context context;

    public Custom_Notification(Context context) {
        this.context = context;
        builder = new NotificationCompat.Builder(context);
        notification = new Notification();
    }

    public void showVaccination(String title, String message) {
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setTicker(message);
        notification = builder.build();
        NotificationManagerCompat.from(context).notify(0, notification);
    }
}
