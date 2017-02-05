package com.example.kinit.e_medicalrecord.General.Classes.Notification;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.example.kinit.e_medicalrecord.R;

public class Custom_Notification {
    private final static String GROUP_VACCINE = "group_vaccine";
    private NotificationCompat.Builder builder;
    private Notification notification;
    private Context context;

    public Custom_Notification(Context context) {
        this.context = context;
        builder = new NotificationCompat.Builder(context);
        notification = new Notification();
    }

    public void showVaccinationToBeExpired(String title, String message) {
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setTicker(message)
                .setGroup(GROUP_VACCINE)
                .build();
        NotificationManagerCompat.from(context).notify(0, notification);
    }

    public void showVaccinationExpired(String title, String message){
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setTicker(message)
                .setGroup(GROUP_VACCINE)
                .build();
        NotificationManagerCompat.from(context).notify(1, notification);
    }
}
