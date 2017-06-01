package com.basic.mychat.mychatbasic.controller;

import com.basic.mychat.mychatbasic.model.Message;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MessageController {

    public static String makeMsgFrame() {
        String messageKey = FirebaseDatabase.getInstance().getReference().child("messages").push().getKey();

        Message message = new Message();
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/messages/" + messageKey, messageValue);


        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        return messageKey;
    }

    public static void editMsg(String messageKey, Message message) {
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child("messages").updateChildren(childUpdates);
    }

    public static void updateMsg(String messageKey, String userName, String messageBody) {
        Message message = new Message();
        message.userName = userName;
        message.message = messageBody;

        editMsg(messageKey, message);
    }
}
