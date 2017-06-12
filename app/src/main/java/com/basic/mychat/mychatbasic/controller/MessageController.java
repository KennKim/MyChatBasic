package com.basic.mychat.mychatbasic.controller;

import com.basic.mychat.mychatbasic.model.Message;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MessageController {

    private final static String CHILD_MESSAGE = "messages";

    public static String getBlankKey() {
        String messageKey = FirebaseDatabase.getInstance().getReference().child(CHILD_MESSAGE).push().getKey();
        return messageKey;
    }
    public static void insertDB(String messageKey, String userName, String messageBody, String photoUrl, String imageUrl) {
        Message message = new Message(userName, messageBody, photoUrl, imageUrl);

        Map<String, Object> messageValue = message.getMsg();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child(CHILD_MESSAGE).updateChildren(childUpdates);

    }

}
