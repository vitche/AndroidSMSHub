package com.vitche.sms.hub.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vitche.sms.hub.model.Constants;

/**
 * Created by Burmaka V on 02.12.2016.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "myLogs";
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = context.getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS, context.MODE_PRIVATE);
        if (prefs.getBoolean(Constants.ENABLE_SERVICE, true)) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent serviceIntent = new Intent(context, MessageListenerService.class);
                context.startService(serviceIntent);
                Log.d(TAG, "------BootCompleteReceiver : onReceive: ");
            }
        }
    }
}
