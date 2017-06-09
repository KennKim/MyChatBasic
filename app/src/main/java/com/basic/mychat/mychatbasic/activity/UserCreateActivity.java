package com.basic.mychat.mychatbasic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.model.User;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;


public class UserCreateActivity extends BaseActivity {
    private final String TAG = UserCreateActivity.class.getSimpleName();

    /* View Component */
    private EditText etUserName;
    private FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        etUserName = (EditText) findViewById(R.id.user_input_name);

        Button btn = (Button) findViewById(R.id.user_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUserButton();
            }
        });

    }

    private void onClickUserButton() {
        final String strUserName = etUserName.getText().toString();
        if (strUserName.isEmpty()) {
            Toast.makeText(UserCreateActivity.this, "Please input your name", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserCreateActivity.this, "Your name is : " + strUserName, Toast.LENGTH_SHORT).show();
            SharedPreferenceUtil.getInstance().putString(SharedPreferenceUtil.USER_NAME, strUserName);
            User user = new User(strUserName);
            mFirebaseDatabase.getReference("member/")
                    .push()
                    .setValue(user)
                    .addOnSuccessListener(UserCreateActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserCreateActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                        }
                    });

            startActivity(new Intent(this, UserListActivity.class));
        }
    }
}
