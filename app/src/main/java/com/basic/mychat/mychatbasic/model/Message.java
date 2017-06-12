package com.basic.mychat.mychatbasic.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {
    public String userName;
    public String message;
    private String faceUrl;
    private String imageUrl;
    public Boolean isDeleted=false;


    public Message() {
    }

    public Message(String userName, String message, String faceUrl, String imageUrl) {
        this.userName = userName;
        this.message = message;
        if(faceUrl!=null) this.message = faceUrl;
        if(imageUrl!=null) this.message = imageUrl;
        this.isDeleted = false;
    }

    @Exclude
    public Map<String, Object> getMsg() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("message", message);
        result.put("isDeleted", isDeleted);
        return result;
    }

    @Exclude
    public static Message getMsgFromSnap(DataSnapshot snapshot) {
        Message msg = new Message();
        msg.userName = (String) snapshot.child("userName").getValue();
        msg.message = (String) snapshot.child("message").getValue();
        msg.isDeleted = (Boolean) snapshot.child("isDeleted").getValue();
        return msg;
    }
}
