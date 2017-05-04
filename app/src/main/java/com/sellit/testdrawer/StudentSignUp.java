package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class StudentSignUp extends AppCompatActivity {

    //Variables for the buttons and Edit Text Boxes
    EditText usernameBox;
    EditText passwordBox;
    EditText fullName;
    EditText emailBox;

    Button continueBtn;
    Button toSignIn;


    private String TAG = StudentSignUp.class.getSimpleName();
    //References to the Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        //Variables for the inputted text
        usernameBox = (EditText) findViewById(R.id.usernameBox);
        passwordBox = (EditText) findViewById(R.id.passwordBox);
        emailBox = (EditText) findViewById(R.id.emailBox);
        fullName = (EditText) findViewById(R.id.fullName);

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //On Click Listener for the Continue Button
        continueBtn = (Button) findViewById(R.id.ContinueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignInActivity.class));
    }


    private void signUp()
    {
        mAuth.createUserWithEmailAndPassword(emailBox.getText().toString(), passwordBox.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String s = usernameBox.getText().toString();
                            Log.d(TAG, "Username: " + s);
                            FirebaseUser user = mAuth.getCurrentUser();
                            String UID = user.getUid();
                            String userName = usernameBox.getText().toString();
                            String name = fullName.getText().toString();
                            StudentInfo info = new StudentInfo(UID, userName, name,
                                    emailBox.getText().toString(), "default", "default");

                            mDatabase.child("studentInfo").child(UID).setValue(info);
                            startActivity(new Intent(StudentSignUp.this, StudentHomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(StudentSignUp.this, "Authentication failed, user already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    //Function for clicking the Continue button and navigating to the Home Screen


    //Function for clicking the Sign In Button and navigating to it
    private void toSignInBtn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}