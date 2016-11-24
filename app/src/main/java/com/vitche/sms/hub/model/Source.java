package com.vitche.sms.hub.model;

import java.util.ArrayList;

/**
 * Created by Burmaka V on 21.11.2016.
 */
public class Source implements PhoneNumberDataSource{
    private String telNumber;
    private String decription;
    private ArrayList<Message> messages;

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
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
