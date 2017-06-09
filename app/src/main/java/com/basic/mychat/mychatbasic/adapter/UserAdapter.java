package com.basic.mychat.mychatbasic.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.mychat.mychatbasic.R;
import com.basic.mychat.mychatbasic.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final int CHAT_END = 1;
    private static final int CHAT_START = 2;

    private List<User> items;
    private String mId;
    private Context context;

    /**
     * Called when a view has been clicked.
     *
     * @param items Message list
     * @param userName      Device userName
     */
    public UserAdapter(List<User> items, String userName, Context context) {
        this.items = items;
        mId = userName;
        this.context = context;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_END) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user_list, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user_list, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
//        Toast.makeText(context, "aaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
        if (items.get(position).userName.equals(mId)) {
            return CHAT_END;
        }
        return CHAT_START;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv1.setText(items.get(position).userName);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;

        ViewHolder(View v) {
            super(v);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
        }
    }
}