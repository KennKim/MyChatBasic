package com.basic.mychat.mychatbasic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.adapter.MessageAdapter;
import com.basic.mychat.mychatbasic.controller.MessageController;
import com.basic.mychat.mychatbasic.model.Message;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    /* View Component */
    private ListView mListView;
    private MessageAdapter mMessageAdapter;
    private EditText etMessage;
    private Button btnSend;

    /* Firebase */
    private ArrayList<Message> mMessageList = new ArrayList<Message>();
    private ArrayList<String> mMessageKeyList = new ArrayList<String>();
    private DatabaseReference mMessageReference;
    private ValueEventListener mMessageListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        setTitle("My Chat basic");

        mMessageReference = FirebaseDatabase.getInstance().getReference().child("messages");

        mListView = (ListView) findViewById(R.id.main_list);

        etMessage = (EditText) findViewById(R.id.main_input_text);
        btnSend = (Button) findViewById(R.id.main_button);
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessageList.clear();
                mMessageKeyList.clear();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Message msg = Message.parseSnapshot(child);
                    if (msg.isDeleted != null && msg.isDeleted == false) {
                        mMessageList.add(msg);
                        mMessageKeyList.add(child.getKey());
                    }
                }
                updateUserInterface();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "load Message List:onCancelled", databaseError.toException());
            }
        };

        if (mMessageReference != null) {
            mMessageReference.addValueEventListener(messageListener);
        }
        mMessageListener = messageListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMessageListener != null) {
            if (mMessageReference != null) {
                mMessageReference.removeEventListener(mMessageListener);
            }
        }
    }

    private void updateUserInterface() {
        mMessageAdapter = new MessageAdapter(this, mMessageList, mMessageKeyList);
        mListView.setAdapter(mMessageAdapter);
        mListView.setSelection(mMessageAdapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_button:
                onClickSendButton();
                break;
        }
    }

    private void onClickSendButton() {
        String messageBody = etMessage.getText().toString();
        String userName = SharedPreferenceUtil.getInstance().getString(SharedPreferenceUtil.USER_NAME, null);
        String messageKey = MessageController.makeMsgFrame();

        MessageController.updateMsg(messageKey, userName, messageBody);
        etMessage.setText(null);
    }
}
