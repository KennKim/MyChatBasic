package com.basic.mychat.mychatbasic.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userName;
    public Boolean isDeleted=false;

    public User(){
    }

    public User(String userName){
        this.userName=userName;
        this.isDeleted=false;
    }

}
