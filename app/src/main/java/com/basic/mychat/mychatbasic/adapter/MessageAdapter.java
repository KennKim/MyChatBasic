package com.basic.mychat.mychatbasic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.model.Message;

import java.util.ArrayList;


public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Message> mMessageList;
    private ArrayList<String> mMessageKeyList;

    public MessageAdapter(Context context, ArrayList<Message> messageList, ArrayList<String> messageKeyList) {
        this.mContext = context;
        this.mMessageList = messageList;
        this.mMessageKeyList = messageKeyList;
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_message, parent, false);
            view = convertView;
        }
        final Message message = mMessageList.get(position);
        TextView userName = (TextView) view.findViewById(R.id.message_user_name);
        userName.setText(message.userName + "'s ");

        TextView mesageBody = (TextView) view.findViewById(R.id.message_body);
        mesageBody.setText(message.message);

        return view;
    }
}
