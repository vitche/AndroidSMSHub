package com.vitche.sms.hub.model;

import java.util.ArrayList;

/**
 * Created by Burmaka V on 21.11.2016.
 */
public interface PhoneNumberDataSource {
    String getTelNumber();
    void setTelNumber(String telNumber);
    ArrayList<Message> getMessages();
    void setMessages(ArrayList<Message> messages);
    String getDecription();
    void setDecription(String decription);

}
