package com.sellit.testdrawer;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Andrew on 4/28/2017.
 */

public class User
{
    public String email;
    public String UUID;
    public String UserName;
    public String Name;
    public ValueEventListener listener;
    String TAG = User.class.getSimpleName();

    public User()
    {

    }

    public User(FirebaseUser FBUser)
    {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        email = FBUser.getEmail();
        UUID = FBUser.getUid();
        listener = new ValueEventListener()
        {
            public User tempUser = new User();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User u = dataSnapshot.getValue(User.class);
                tempUser.UserName = u.UserName;
                tempUser.Name = u.Name;
                Log.w(TAG, "UserName: " + tempUser.UserName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("userInfo").child(UUID).addListenerForSingleValueEvent(listener);

    }

}
