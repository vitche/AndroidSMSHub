package com.vitche.sms.hub.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Burmaka V on 21.11.2016.
 */
public class Source implements PhoneNumberDataSource{
    private static final String TAG = "myLogs";
    private String phoneNumber;
    private String decription;
    private ArrayList<Message> messages;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String telNumber) {
        this.phoneNumber = telNumber;
    }

    public ArrayList<Message> getMessages() {
        if (messages == null)
            return new ArrayList<Message>();
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        PhoneNumberDataSource second = (PhoneNumberDataSource)o;

        long thisValue =  0;
        long secondValue = 0;
            if (messages.size() > 0)
        thisValue = messages.get(Constants.LAST_SMS_INDEX).getId();
            if (second.getMessages().size() > 0)
        secondValue = second.getMessages().get(Constants.LAST_SMS_INDEX).getId();

        if (thisValue == secondValue)
            result = 0;
        result = thisValue < secondValue ? 1 : -1;
        return result;
    }
}
