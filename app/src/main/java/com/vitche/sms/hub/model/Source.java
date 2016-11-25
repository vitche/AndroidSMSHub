package com.vitche.sms.hub.model;

import java.util.ArrayList;

/**
 * Created by Burmaka V on 21.11.2016.
 */
public class Source implements PhoneNumberDataSource{
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
}
