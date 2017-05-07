package com.sellit.testdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by jrkre on 5/4/2017.
 */


//This was a work in progress fragment that would handle communication between users in-app.
public class ChatFragment extends Fragment{

    RecyclerView recView;
    EditText chatContent;
    ImageView addChat;
    String chatID;
    LayoutInflater inflater;
    String itemOwner;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) 
    {
        chatID = getArguments().getString("chatID");

        return inflater.inflate(R.layout.activity_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        chatContent = (EditText) view.findViewById(R.id.addChatBox);
        addChat = (ImageView) view.findViewById(R.id.addChatButton);
        recView = (RecyclerView) view.findViewById(R.id.chatRecView);



        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatContent.getText().toString();
                ChatInstance c = new ChatInstance(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        itemOwner, message, Calendar.getInstance().getTime(), chatID);
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                String key = mRef.push().getKey();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/chats/"+ key, c.toMap());
                mRef.updateChildren(childUpdates);
            }
        });
        setupRec(view);
        super.onViewCreated(view, savedInstanceState);
    }
    
    private void setupRec(View view) {
        getChatInstance();
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        recView.setItemAnimator(new DefaultItemAnimator());

    }


    public void getChatInstance()
    {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("chats");
        mRef.orderByChild("chatID").equalTo(chatID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final ArrayList<ChatInstance> listChat = new ArrayList<>();
                HashMap<String, Object> messages = (HashMap<String, Object>) dataSnapshot.getValue();
                if (messages == null)
                {
                    return;
                }
                Set keys = messages.keySet();
                Object[] chatList = messages.values().toArray();
                final ChatListAdapter adapter = new ChatListAdapter(listChat, getActivity());
                for (int i = 0; i < chatList.length; i++) {
                    Object item = chatList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String, Object> itemMap = (HashMap<String, Object>) item;

                    final ChatInstance chatTemp = new ChatInstance();
                    chatTemp.Key = Key;
                    chatTemp.buyer = (String) itemMap.remove("buyer");
                    Log.e(TAG, "Buyer UID: " + chatTemp.buyer);
                    chatTemp.content = (String) itemMap.remove("content");
                    Log.e(TAG, "Content " + chatTemp.content);
                    chatTemp.seller = (String) itemMap.remove("seller");
                    chatTemp.chatID = (String) itemMap.remove("chatID");
                    listChat.add(chatTemp);
                }

                Log.d(TAG, "Num messages: " + listChat.size());
                recView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
