package com.sellit.testdrawer;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Andrew on 4/28/2017.
 */

//Class made for defining a students properties
public class Student
{
    public String email;
    public String UUID;
    public String UserName;
    public String Name;
    public ValueEventListener listener;
    String TAG = Student.class.getSimpleName();

    public Student()
    {

    }

    public Student(FirebaseUser FBUser)
    {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        email = FBUser.getEmail();
        UUID = FBUser.getUid();
        listener = new ValueEventListener()
        {
            public Student tempUser = new Student();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Student u = dataSnapshot.getValue(Student.class);
                tempUser.UserName = u.UserName;
                tempUser.Name = u.Name;
                Log.w(TAG, "userName: " + tempUser.UserName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("studentInfo").child(UUID).addListenerForSingleValueEvent(listener);

    }

}
