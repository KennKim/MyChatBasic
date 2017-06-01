package com.basic.mychat.mychatbasic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;


/**
 * Created by namhoonkim on 20/02/2017.
 */

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = UserActivity.class.getSimpleName();

    /* View Component */
    private EditText mUserInputName;
    private Button mUserButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        init();
    }

    private void init() {
        mUserInputName = (EditText) findViewById(R.id.user_input_name);
        mUserButton = (Button) findViewById(R.id.user_button);
        mUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_button:
                onClickUserButton();
                break;
            default:
                break;
        }
    }

    private void onClickUserButton() {
        String userName = mUserInputName.getText().toString();
        if (userName == null || userName.length() < 1) {
            Toast.makeText(UserActivity.this,"Please input your name",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(UserActivity.this,"Your name is : ",Toast.LENGTH_SHORT).show();
            SharedPreferenceUtil.getInstance().putString(SharedPreferenceUtil.USER_NAME, userName);

            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
