package com.basic.mychat.mychatbasic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {


    private final String TAG = ChatRoomActivity.class.getSimpleName();

    private final static String CHILD_MESSAGE = "messages";
    private final static int REQUEST_IMAGE = 1002;
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static String texting = "";

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
         * Firebase - Initialize
         */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(CHILD_MESSAGE);

        init();

        // 작성중인 text 복원.
        if (texting.length() > 0) {
            etMessage.setText(texting);
            texting = "";
        }
    }

    @Override
    protected void onDestroy() {
        if (etCheck()) {
            texting = etMessage.getText().toString();
        }
        super.onDestroy();
    }

    private Boolean etCheck() {
        String text = etMessage.getText().toString();
        if (text.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void init() {
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 앨범 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_IMAGE);

                /*
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image*//*");
                startActivityForResult(intent, REQUEST_IMAGE);*/
            }
        });
        etMessage = (EditText) findViewById(R.id.et_msg_body);
        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCheck()) {
                    onClickSendButton(etMessage.getText().toString());
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());
                    Message message = new Message(userName, null, null, LOADING_IMAGE_URL);
                    mDatabaseReference.push()
                            .setValue(message, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference = FirebaseStorage.getInstance()
                                                .getReference(userName)
                                                .child(key)
                                                .child(uri.getLastPathSegment());

                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.",
                                                databaseError.toException());
                                    }
                                }
                            });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri)
                .addOnCompleteListener(ChatRoomActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Message message = new Message(userName, null, null, task.getResult().getDownloadUrl().toString());
                            mDatabaseReference.child(key).setValue(message);
                        } else {
                            Log.w(TAG, "Image upload task was not successful.", task.getException());
                        }
                    }
                });
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
        MessageController.insertDB(key, userName, msgBody, null, null);
        etMessage.setText(null);
    }

    private void sendPhoto(Intent data) {

    }
}
