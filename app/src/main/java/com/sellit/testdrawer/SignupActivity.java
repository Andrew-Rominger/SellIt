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

public class SignupActivity extends AppCompatActivity {

    //Variables for the buttons and Edit Text Boxes
    Button toBase;
    Button toSignIn;
    EditText usernameInput;
    EditText passwordInput;
    EditText emailInput;

    //References to the Firebase
    FirebaseDatabase database;
    private DatabaseReference mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Variables for the inputed text
        usernameInput = (EditText) findViewById(R.id.UserNameBox);
        passwordInput = (EditText) findViewById(R.id.PasswordBox);
        emailInput = (EditText) findViewById(R.id.EmailBox);

        //Firebase Initialization
        mFirebaseRef = FirebaseDatabase.getInstance().getReference();

        //On Click Listener for the Continue Button
        toBase = (Button) findViewById(R.id.ContinueBtn);
        toBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBase();
            }
        });

        //On Click listener for the Sign In Button
        toSignIn = (Button) findViewById(R.id.toSignInBtn);
        toSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toSignInBtn();
            }
        });
    }

    //Function for retrieving the user inputted data
    public void retrieve () {
        //Final Stringed Variables of the inouted data
        final String entUsername = usernameInput.getText().toString();
        final String entPassword = passwordInput.getText().toString();
        final String entEmail = emailInput.getText().toString();

        //Adds an If Else statement to check if the username submitted is already one and if any field was left empty
        mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Variable that changes the list of users from the firebase into a string
                String users = dataSnapshot.child("users").getValue().toString();
                //If statement that checks if the username is already in the list other wise toasts "Username Taken"
                if ((entUsername).equals(users)){
                    Toast.makeText(SignupActivity.this, "Username Taken", Toast.LENGTH_LONG).show();
                    //If the username, password or email has no information typed in the it will toast "All fields must be filled in"
                }else if((entUsername).equals(" ") || (entPassword).equals(" ") || (entEmail.equals(" "))){
                    Toast.makeText(SignupActivity.this, "All fields must be filled in", Toast.LENGTH_LONG).show();
                    //Else the screen will navigate to the Home Screen
                }else{
                    toBase();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignupActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Function for clicking the Continue button and navigating to the Home Screen
    private void toBase(){
        writeNewUser(usernameInput.getText().toString(), passwordInput.getText().toString(), emailInput.getText().toString());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //Function for clicking the Sign In Button and navigating to it
    private void toSignInBtn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    //Function for writing the inputed data in the Firebase
    private void writeNewUser(String userId, String password, String email) {
        //mFirebaseRef.child("users").setValue(userId.toString());
        mFirebaseRef.child("users").child(userId).child("password").setValue(password);
        mFirebaseRef.child("users").child(userId).child("email").setValue(email);
        mFirebaseRef.child("users").child(userId).child("username").setValue(userId);
    }
}