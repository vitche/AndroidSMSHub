package com.vitche.sms.hub.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vitche.sms.hub.R;

/**
 * Created by Burmaka V on 27.11.2016.
 */
public class SMSNotification {
    private static final String TAG = "myLogs";
    static int notificationId = 1;

    static NotificationManager notificationManager;
    static Notification.Builder mBuilder;
    private static int messagesNum;

    public static void initNotification(Context ctx, String sourcesNumber) {
        notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx, SMSHubMainActivity.class);

        mBuilder = new Notification.Builder(ctx);
        Notification notification = mBuilder
                .setContentTitle(sourcesNumber + " " + ctx.getString(R.string.notification_title)) // TODO set and update source number
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        notificationManager.notify(notificationId, notification);

    }

    public static void updateNotification(Context ctx, String sourcesNumber) {
//        messagesNum = 5;
        Log.d(TAG, "------SMSNotification : updateNotification: messagesNum = " + messagesNum);
        if (notificationManager != null) {
            if (mBuilder != null) {
                if (sourcesNumber != null && !sourcesNumber.isEmpty())
                    mBuilder.setContentTitle(sourcesNumber + " " + ctx.getString(R.string.notification_title));

                if (messagesNum > 0) {
                    mBuilder.setContentText(messagesNum + " " + ctx.getString(R.string.notification_content));
                }else {
                    mBuilder.setContentText(" ");
                }
                            mBuilder.setNumber(messagesNum)
                            .setAutoCancel(false);
                Notification notification = mBuilder.build();

                Intent intent = new Intent(ctx, SMSHubMainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.contentIntent = pendingIntent;

                notificationManager.notify(notificationId, notification);
            }

        }
    }

    public static void cancelNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) ctx.getSystemService(ns);
        notificationManager.cancel(notificationId);
        mBuilder = null;
    }

    public static int getMessagesNum() {
        return messagesNum;
    }

    public static void setMessagesNum(int messagesNum) {
        SMSNotification.messagesNum = messagesNum;
    }
}
