package com.sellit.testdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class StudentSettings extends AppCompatActivity implements View.OnClickListener {

    Button saveButton;

    Spinner stateSpinner;

    EditText fullName;
    EditText email;
    EditText city;

    TextView Username;

    String TAG = StudentSettings.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_settings);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        String[] states = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        stateSpinner = (Spinner) findViewById(R.id.spinnerStudentSettings);
        stateSpinner.setAdapter(adapter);

        fullName = (EditText) findViewById(R.id.studentFullName);
        email = (EditText) findViewById(R.id.studentEmail);
        city = (EditText) findViewById(R.id.cityInput);
        saveButton = (Button) findViewById(R.id.studentSaveBtn);
        Username = (TextView) findViewById(R.id.studentUsername);

        saveButton.setOnClickListener(this);

        FirebaseUser FBUser = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(FBUser.getEmail());
        city.setText(mRef.child("studentInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("city").toString());
        stateSpinner.setPrompt(mRef.child("studentInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("state").toString());

        String UUID = FBUser.getUid();
        ValueEventListener listener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                StudentInfo s = dataSnapshot.getValue(StudentInfo.class);
                fullName.setText(s.fullName);
                city.setText(s.city);
                Username.setText(s.userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("studentInfo").child(UUID).addListenerForSingleValueEvent(listener);
    }

    @Override
    public void onClick(View v)
    {
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email.getText().toString());
        StudentInfo newData = new StudentInfo();
        newData.city = city.getText().toString();
        newData.fullName = fullName.getText().toString();
        newData.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        newData.state = stateSpinner.getSelectedItem().toString();
        newData.email = email.getText().toString();
        newData.TAG = "StudentInfo";
        newData.userName = Username.getText().toString();

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> newDataMap = newData.toStudentMap();
        childUpdates.put("/studentInfo/"+newData.uid, newDataMap);
        dRef.updateChildren(childUpdates);
        Toast.makeText(this, "Saved Settings", Toast.LENGTH_LONG).show();
    }
}
