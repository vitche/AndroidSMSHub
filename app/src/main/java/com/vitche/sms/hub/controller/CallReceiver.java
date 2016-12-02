package com.vitche.sms.hub.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Burmaka V on 02.12.2016.
 */
public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            Log.d(TAG, "------CallReceiver : onReceive: ");
    }
}
