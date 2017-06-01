package com.basic.mychat.mychatbasic.activity;

import android.content.Intent;
import android.os.Bundle;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;
import com.basic.mychat.mychatbasic.util.UserUtil;

public class StartActivity extends BaseActivity {
    private final String TAG = StartActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferenceUtil.getInstance().init(getApplicationContext());

        String userName = UserUtil.loadUserName();
        switchActivity(userName);
    }


    private void switchActivity(String userName) {
        if (userName == null || userName.length() < 1) {
            startActivity(new Intent(StartActivity.this, UserActivity.class));
            finish();
        } else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }


}
