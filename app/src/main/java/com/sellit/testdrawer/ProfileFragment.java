package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ProfileFragment extends Fragment {

    View myView;

    FloatingActionButton toSettings;

    TextView firstName;
    TextView location;

    String uid;

    UserInfo userPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_profile,container,false);
        Bundle args = getArguments();
        uid = args.getString("uid");

        firstName = (TextView) myView.findViewById(R.id.profileFirstName);
        location = (TextView) myView.findViewById(R.id.profileLocation);

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        ValueEventListener listener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                userPage = dataSnapshot.getValue(UserInfo.class);

                firstName.setText(userPage.fullName);
                location.setText(userPage.city + ", " + userPage.state);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("userInfo").child(uid).addListenerForSingleValueEvent(listener);
        settings();

        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);

        return myView;
    }

    private void settings(){
        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);
        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettingsMeth();
            }
        });
    }

    private void toSettingsMeth(){
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        startActivity(intent);
    }
}