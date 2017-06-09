package com.basic.mychat.mychatbasic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.model.Message;

import java.util.List;

/**
 * Created by conscious on 2017-06-09.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    private List<Message> items;
    private String mId;
    private Context context;

    /**
     * Called when a view has been clicked.
     *
     * @param items    Message list
     * @param userName Device userName
     */
    public MsgAdapter(List<Message> items, String userName, Context context) {
        this.items = items;
        mId = userName;
        this.context = context;
    }

    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_mychat, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chat, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).userName.equals(mId)) {
            return CHAT_END;
        }
        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvUserName.setText(items.get(position).userName);
        holder.tvMsgBody.setText(items.get(position).message);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvMsgBody;

        ViewHolder(View v) {
            super(v);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvMsgBody = (TextView) itemView.findViewById(R.id.tv_msg_body);
        }
    }
}
