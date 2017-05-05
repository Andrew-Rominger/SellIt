package com.sellit.testdrawer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jrkre on 5/4/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>
{
    ArrayList<ChatInstance> listChat;
    Context context;
    LayoutInflater inflater;
    Activity A;
    String TAG = ChatListAdapter.class.getSimpleName();
    
    public ChatListAdapter(ArrayList<ChatInstance> chat, Activity A)
    {
        Log.d(TAG, "Chat Adapter Constructor");
        listChat = chat;
        this.A = A;
        this.context = this.A.getApplicationContext();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.d(TAG, "OnCreateViewHolder");
        return new ChatListAdapter.ChatViewHolder(inflater.inflate(R.layout.chat_in_list, parent));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position)
    {
        Log.d(TAG, "BindViewHolder");

        holder.setData(listChat.get(position));
    }

    public int getItemCount()
    {
        return listChat.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View chatView;
        TextView itemBuyer;
        TextView itemSeller;
        TextView messageContent;
        FrameLayout wrapper;
        ChatInstance chat;

        public ChatViewHolder(View chatView)
        {
            super(chatView);
            Log.d(TAG, "ChatViewHolder Constructor");
            this.chatView = chatView;
            messageContent = (TextView) chatView.findViewById(R.id.chatContent);

            wrapper = (FrameLayout) chatView.findViewById(R.id.CIL_Wrapper);
            wrapper.setOnClickListener(this);
        }

        public void setData(ChatInstance chat)
        {
            this.chat = chat;
            Log.d(TAG, "setData");
            messageContent.setText(chat.chatContent);
            itemSeller.setText(chat.sellerUID);
            itemBuyer.setText(chat.buyerUID);
        }

        @Override
        public void onClick(View v)
        {
            return;
        }
    }
}
