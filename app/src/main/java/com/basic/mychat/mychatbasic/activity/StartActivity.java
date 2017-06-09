package com.basic.mychat.mychatbasic.activity;

import android.content.Intent;
import android.os.Bundle;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferenceUtil.getInstance().init(getApplicationContext());
        String userName = SharedPreferenceUtil.getInstance().getString(SharedPreferenceUtil.USER_NAME, null);


        if (userName == null || userName.isEmpty()) {
            startActivity(new Intent(StartActivity.this, UserCreateActivity.class));
            finish();
        } else {
            startActivity(new Intent(StartActivity.this, UserListActivity.class));
            finish();
        }

    }


}
