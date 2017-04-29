package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
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
import com.google.firebase.auth.UserProfileChangeRequest;
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

    private String TAG = SignupActivity.class.getSimpleName();
    //References to the Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Variables for the inputed text
        usernameInput = (EditText) findViewById(R.id.UserNameBox);
        passwordInput = (EditText) findViewById(R.id.PasswordBox);
        emailInput = (EditText) findViewById(R.id.EmailBox);

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //On Click Listener for the Continue Button
        toBase = (Button) findViewById(R.id.ContinueBtn);
        toBase.setOnClickListener(new View.OnClickListener() {
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
        mAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String s = usernameInput.getText().toString();
                            Log.d(TAG, "Username: " + s);
                            FirebaseUser user = mAuth.getCurrentUser();
                            String UID = user.getUid();
                            String userName = usernameInput.getText().toString();
                            String name = "";
                            String phoneNumber = "";

                            UserInfo info = new UserInfo(UID, userName, name, phoneNumber, emailInput.getText().toString());

                            mDatabase.child("userInfo").child(UID).setValue(info);
                            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
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

    //Function for writing the inputed data in the Firebase

}