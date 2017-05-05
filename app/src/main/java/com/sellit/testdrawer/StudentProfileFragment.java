package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class StudentProfileFragment extends Fragment {

    View myView;

    FloatingActionButton toStudentSettings;

    TextView fullName;
    TextView outOf;
    TextView goalName;

    String uid;

    StudentInfo studentPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_student_profile, container, false);
        Bundle args = getArguments();
        fullName = (TextView) myView.findViewById(R.id.fullName);
        outOf = (TextView) myView.findViewById(R.id.outOf);
        goalName = (TextView) myView.findViewById(R.id.currentGoal);
        uid = args.getString("uid");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentPage = dataSnapshot.getValue(StudentInfo.class);
                fullName.setText(studentPage.fullName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("studentInfo").child(uid).addListenerForSingleValueEvent(listener);
        settings();
        toStudentSettings = (FloatingActionButton) myView.findViewById(R.id.toStudentSettings);

        mRef.child("studentInfo").child(uid).child("Goal").child("goalName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                goalName.setText(snapshot.getValue().toString());
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
        mRef.child("studentInfo").child(uid).child("Goal").child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                outOf.setText("0/" + snapshot.getValue().toString());
            }
            @Override public void onCancelled(DatabaseError error) { }
        });


        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fullName = (TextView) view.findViewById(R.id.fullName);
    }

    private void settings() {
        toStudentSettings = (FloatingActionButton) myView.findViewById(R.id.toStudentSettings);
        toStudentSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettingsMeth();
            }
        });
    }

    private void toSettingsMeth() {
        Intent intent = new Intent(getActivity(), StudentSettings.class);
        startActivity(intent);
    }
}