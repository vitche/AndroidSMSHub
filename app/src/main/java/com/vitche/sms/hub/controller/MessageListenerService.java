package com.vitche.sms.hub.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.view.MainActivity;

public class MessageListenerService extends Service {
    private static final String TAG = "myLogs";
    public static final String SMS_RECEIVER_TAG = "SMS_RECEIVER_TAG";

    int notificationId;
    BroadcastReceiver smsReceiver;
    NotificationManager notificationManager;
    Notification.Builder mBuilder;

    public MessageListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "------MessageListenerService : onCreate: ");
        smsReceiver = new SMSReceiver();

        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVER_TAG);
        registerReceiver(smsReceiver, intentFilter);
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "------MessageListenerService : onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        cancelNotification();
        Log.d(TAG, "------MessageListenerService : onDestroy: ");
    }

    private void initNotification() {
        notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);

        mBuilder = new Notification.Builder(this);
        Notification notification = mBuilder
                .setContentTitle("setContentTitle") // TODO set and update source number
                .setContentText("setContentText")
                .setSmallIcon(R.drawable.ic_service_notification)
                .setAutoCancel(false)
                .setNumber(5) // TODO set and update number recieved sms
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        notificationManager.notify(notificationId, notification);

    }

    private void cancelNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) this.getSystemService(ns);
        notificationManager.cancel(notificationId);
    }


}
