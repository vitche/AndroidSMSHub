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
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.view.MainActivity;
import com.vitche.sms.hub.view.SMSNotification;

public class MessageListenerService extends Service {
    private static final String TAG = "myLogs";
    public static final String SMS_RECEIVER_TAG = "SMS_RECEIVER_TAG";

    BroadcastReceiver smsReceiver;

    public MessageListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "------MessageListenerService : onCreate: ");
        smsReceiver = new SMSReceiver();

        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVER_TAG);
        registerReceiver(smsReceiver, intentFilter);
        int sourceNumber = getSourcesNumber();
        if ( sourceNumber > 0){
            SMSNotification.initNotification(this, "" + sourceNumber);
        }else {
            Toast.makeText(MessageListenerService.this, "Not started, add source", Toast.LENGTH_SHORT).show();
        }

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
        SMSNotification.cancelNotification(this);
        Log.d(TAG, "------MessageListenerService : onDestroy: ");
    }


    private int getSourcesNumber(){
        return SourceDB.getAllSorces(this).size();
    }

}
