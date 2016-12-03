package com.vitche.sms.hub.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

import com.vitche.sms.hub.controller.db.MessageDB;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Message;
import com.vitche.sms.hub.model.PhoneNumberDataSource;
import com.vitche.sms.hub.view.SMSNotification;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Burmaka V on 24.10.2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "myLogs";

    //    TODO notification icon
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "------MessageListenerService : onReceive: ");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        long timeStamp = msgs[i].getTimestampMillis();
                        Log.d(TAG, "------MessageListenerService : onReceive: =========== " + msgBody + " , from " + msg_from);
                        checkSMS(context, msg_from, msgBody, timeStamp);
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

    private void checkSMS(Context context, String msg_from, String msgBody, long timeStamp) {
        List<PhoneNumberDataSource> sources = SourceDB.getAllSorces(context);

        if (sources != null)
            for (int i = 0; i < sources.size(); i++) {
                String phoneNumber = sources.get(i).getPhoneNumber();
                if (phoneNumber.equals(msg_from) || ("+38" + phoneNumber).equals(msg_from)) {
                    MessageDB.insertMessage(context, phoneNumber, timeStamp, msgBody);
                    listsInvalidate();
                    SMSNotification.setMessagesNum(SMSNotification.getMessagesNum() + 1);
                    SMSNotification.updateNotification(context, null);
//                    deleteSMS(context, msgBody, msg_from);
                } else if (msgBody != null && msgBody.contains(phoneNumber)) {
                    MessageDB.insertMessage(context, phoneNumber, timeStamp, msgBody);
                    listsInvalidate();
                    SMSNotification.setMessagesNum(SMSNotification.getMessagesNum() + 1);
                    SMSNotification.updateNotification(context, null);
//                    deleteSMS(context, msgBody, msg_from);
                }
            }
    }


    /**
     * Not working on 4.4 and higher, i.e. newer
     * @param context
     * @param message
     * @param number
     */
    private void deleteSMS(final Context context, final String message, final String number) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Uri uriSms = Uri.parse("content://sms/inbox");
                    Cursor c = context.getContentResolver().query(uriSms,
                            new String[]{"_id", "thread_id", "address",
                                    "person", "date", "body"}, null, null, null);

                    if (c != null && c.moveToFirst()) {
                        do {

                            long id = c.getLong(0);
                            int thread_id = c.getInt(1);
                            String address = c.getString(2);
                            String body = c.getString(5);
                            Log.d(TAG, "------SMSReceiver : deleteSMS: id = " + id + "  thread id = " + thread_id);

                            if (message.equals(body) && address.equals(number)) {
//                                context.getContentResolver().delete(
//                                        Uri.parse("content://sms/" + id), null, null);
                                context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id),null,null);
                                Log.d(TAG, "------SMSReceiver : deleteSMS: deleted :" + number + body + id + " thr id = " + thread_id);
                                break;
                            }
                        } while (c.moveToNext());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "------SMSReceiver : deleteSMS: Could not delete SMS from inbox: " + e.getMessage());
                }
            }
        };
        handler.postDelayed(runnable, 5000);

    }


    private void listsInvalidate() {
//        TODO invalidate messages and sources lists
    }

}
