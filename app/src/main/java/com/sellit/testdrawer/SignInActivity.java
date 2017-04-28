package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class SignInActivity extends AppCompatActivity {

    //Variables for the Buttons and Edit Text Boxes
    Button signInBtn;
    Button toCreateAcc;

    EditText username;
    EditText password;

    FirebaseDatabase database;
    private DatabaseReference mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = (EditText) findViewById(R.id.signInName);
        password = (EditText) findViewById(R.id.signInPassword);

        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieve();
            }
        });

        toCreateAcc = (Button) findViewById(R.id.toCreateAccBtn);
        toCreateAcc.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               toCreateAcc();
           }
        });

        //Firebase Initialization
        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    //Function to retrieve information from the Firebase and to check if the correct information is submitted
    public void retrieve () {
        //Final Stringed variables of the enter information
        final String entUsername = username.getText().toString();
        final String entPassword = password.getText().toString();

        //Checks to see if the information submitted is a valid login
        mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean ifThere = dataSnapshot.child("users").child(entUsername).child("username").exists();
                if(ifThere){
                    String username = dataSnapshot.child("users").child(entUsername).child("username").getValue().toString();
                    if ((entUsername).equals(username)){
                        String password = dataSnapshot.child("users").child(entUsername).child("password").getValue().toString();
                        if ((entPassword).equals(password)){
                            toBrowse();
                        }else{
                            Toast.makeText(SignInActivity.this, "Password Does Not Match", Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    Toast.makeText(SignInActivity.this, "Username Does Not Match", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Function to navigate to the Home Screen
    private void toBrowse(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //Function to navigate to the Create Account Screen
    private void toCreateAcc() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}

