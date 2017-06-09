package com.basic.mychat.mychatbasic.util;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by conscious on 2017-06-09.
 */

public class MyFirebaseDB extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
