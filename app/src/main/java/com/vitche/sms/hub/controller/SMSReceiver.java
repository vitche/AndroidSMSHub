package com.vitche.sms.hub.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
//                            Log.d("Exception caught",e.getMessage());
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
                    SMSNotification.setMessagesNum(SMSNotification.getMessagesNum()+1);
                    SMSNotification.updateNotification(context, null);
                }
            }
    }

    private void listsInvalidate() {
//        TODO invalidate messages and sources lists
    }

    class MessageComparator implements Comparator<Message> {

        @Override
        public int compare(Message message, Message t1) {
            if (message.getId() > t1.getId()) {
                return 1;
            } else if (message.getId() < t1.getId()) {
                return -1;
            }
            return 0;
        }
    }
}
