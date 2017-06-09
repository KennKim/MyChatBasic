package com.basic.mychat.mychatbasic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.adapter.MsgAdapter;
import com.basic.mychat.mychatbasic.controller.MessageController;
import com.basic.mychat.mychatbasic.model.Message;
import com.basic.mychat.mychatbasic.util.SharedPreferenceUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private final String TAG = ChatRoomActivity.class.getSimpleName();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    /* View Component */
    private RecyclerView mRecyclerView;
    private MsgAdapter mAdapter;
    private ArrayList<Message> items;
    private String userName;

    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        setTitle("My Chat basic");

        items = new ArrayList<>();
        userName = SharedPreferenceUtil.getInstance().getString(SharedPreferenceUtil.USER_NAME, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new MsgAdapter(items, userName, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        /**
         * Firebase - Inicialize
         */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("messages");

        init();
    }

    private void init() {
        etMessage = (EditText) findViewById(R.id.main_input_text);
        Button btnSend = (Button) findViewById(R.id.main_button);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                if (text.isEmpty()) return;
                onClickSendButton(text);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        Message message = dataSnapshot.getValue(Message.class);
                        items.add(message);
                        mRecyclerView.scrollToPosition(items.size() - 1);
                        mAdapter.notifyItemInserted(items.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatRoomActivity.this, "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        };
        if (mDatabaseReference != null) {
            mDatabaseReference.addChildEventListener(childEventListener);
        }
        mChildEventListener = childEventListener;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mChildEventListener != null && mDatabaseReference != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }

    }

/*
    private void onClickSendButton2() {

        String msgBody = etMessage.getText().toString();
        String userName = SharedPreferenceUtil.getInstance().getString(SharedPreferenceUtil.USER_NAME, null);
        String key = mFirebaseDatabase.getReference().child("messages").push().getKey();

        Message message = new Message();
        message.userName = userName;
        message.message = msgBody;
        message.isDeleted = false;

        Map<String, Object> messageValue = message.getMsg();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, messageValue);

        mFirebaseDatabase.getReference().child("messages").updateChildren(childUpdates);
        etMessage.setText(null);
    }*/

    private void onClickSendButton(String msgBody) {
        String key = MessageController.getBlankKey();
        MessageController.insertDB(key, userName, msgBody);
        etMessage.setText(null);
    }
}
