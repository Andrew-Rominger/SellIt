package com.sellit.testdrawer;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Andrew on 4/28/2017.
 */

public class UserInfo
{
    public String UserName;
    public String PhoneNumber;
    public String Name;
    public String UID;
    public String Email;
    public String TAG = UserInfo.class.getSimpleName();
    private DatabaseReference mDatabase;
    public UserInfo()
    {

    }

    public UserInfo(String UUID, String UserName, String Name, String PhoneNumber, String Email)
    {
        this.UID = UUID;
        this.UserName = UserName;
        this.Name = Name;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
    }
    public UserInfo(String UUID)
    {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Log.d(TAG, "String: " + "server/nationals-master/userInfo/"+ UUID);
        DatabaseReference mRef = mDatabase.getReference("server/nationals-master/userInfo/"+ UUID);
        this.UID = UUID;
        ValueEventListener listner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                UserInfo ret = dataSnapshot.getValue(UserInfo.class);
                UserInfo.this.UserName = ret.UserName;
                UserInfo.this.PhoneNumber = ret.PhoneNumber;
                UserInfo.this.Name = ret.Name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addListenerForSingleValueEvent(listner);

    }


}
