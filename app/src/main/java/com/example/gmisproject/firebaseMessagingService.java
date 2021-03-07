package com.example.gmisproject;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class firebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        initChannels(this);

        String notificationTitle = remoteMessage.getData().get("title");
        String notificationMessage = remoteMessage.getData().get("body");
        String clickActionNotification = remoteMessage.getData().get("click_action");

        NotificationCompat.Builder mBuilder;

        // content of notification for bins

        int notificationId = (int) System.currentTimeMillis();
        mBuilder = new NotificationCompat.Builder(this, "default")
                // payLoad from function in firebase
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Log.i("notificationId", String.valueOf(notificationId));


        // click action for opening profile
        Intent intent = new Intent(clickActionNotification);
        Bundle bundle = new Bundle();

        bundle.putString("userName", notificationMessage);
        bundle.putInt("notificationId", notificationId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());


    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {

                AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(getApplicationContext(), firebaseMessagingService.class);
                PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intent2, 0);
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000), pi);
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), firebaseMessagingService.class), PendingIntent.FLAG_UPDATE_CURRENT));

            }
        }.run();


        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), firebaseMessagingService.class), PendingIntent.FLAG_UPDATE_CURRENT));


    }
}


