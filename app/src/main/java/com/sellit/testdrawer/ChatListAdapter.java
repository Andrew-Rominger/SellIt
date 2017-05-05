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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        return new ChatListAdapter.ChatViewHolder(inflater.inflate(R.layout.chat_in_list, parent, false));
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

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        View chatView;
        TextView userName;
        TextView messageContent;
        FrameLayout wrapper;
        ChatInstance chat;

        public ChatViewHolder(View chatView)
        {
            super(chatView);
            Log.d(TAG, "ChatViewHolder Constructor");
            this.chatView = chatView;
            messageContent = (TextView) chatView.findViewById(R.id.chatContent);
            userName = (TextView) chatView.findViewById(R.id.posterUserName);

        }


        public void setData(ChatInstance chat)
        {
            this.chat = chat;
            Log.d(TAG, "setData");
            messageContent.setText(chat.content);
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            ValueEventListener listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    UserInfo u = dataSnapshot.getValue(UserInfo.class);
                    if(u == null)
                    {
                        userName.setText("User Name not found");

                    }else
                    {
                        userName.setText(u.userName);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mRef.child("studentInfo").child(chat.buyer).addListenerForSingleValueEvent(listener);
        }

    }
}
