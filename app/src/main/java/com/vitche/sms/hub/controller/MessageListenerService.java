package com.vitche.sms.hub.controller;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.view.SMSNotification;

public class MessageListenerService extends Service {
    private static final String TAG = "myLogs";
    public static final String SMS_RECEIVER_TAG = "SMS_RECEIVER_TAG";
    public static final String CALL_RECEIVER_TAG = "CALL_RECEIVER_TAG";

    BroadcastReceiver smsReceiver;
    BroadcastReceiver callReceiver;

    public MessageListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "------MessageListenerService : onCreate: ");

        callReceiver = new CallReceiver();

        IntentFilter callIntentFilter = new IntentFilter();
        callIntentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(callReceiver, callIntentFilter);


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

        if (callReceiver != null) {
            unregisterReceiver(callReceiver);
        }
        SMSNotification.cancelNotification(this);
        Log.d(TAG, "------MessageListenerService : onDestroy: ");
    }


    private int getSourcesNumber(){
        return SourceDB.getAllSorces(this).size();
    }

}
