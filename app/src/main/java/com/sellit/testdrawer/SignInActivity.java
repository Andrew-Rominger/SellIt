package com.sellit.testdrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class SignInActivity extends AppCompatActivity {

    //Variables for the Buttons and Edit Text Boxes
    Button recoverPwBtn;

    Button signInBtn;
    Button toCreateAcc;

    EditText userEmail;
    EditText password;

    FirebaseDatabase database;

    String sStud;
    String isStudent;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String m_Text;

    private String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        m_Text = "";
        recoverPwBtn = (Button) findViewById(R.id.recoverPwBtn);
        recoverPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptEmail();
            }
        });

        userEmail = (EditText) findViewById(R.id.signInEmail);
        password = (EditText) findViewById(R.id.signInPassword);

        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        toCreateAcc = (Button) findViewById(R.id.toCreateAccBtn);
        toCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCreateAcc();
            }
        });

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {

    }

    //Function to retrieve information from the Firebase and to check if the correct information is submitted
    private void signInUser() {
        if (!userEmail.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            mAuth.signInWithEmailAndPassword(userEmail.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String UID = user.getUid();
                                Log.d(TAG, UID);
                                String isStudentUID = mDatabase.child("studentInfo").child(UID).toString();
                                isStudent = isStudentUID.replaceAll("https://nationals-master.firebaseio.com/studentInfo/", "");
                                Log.d(TAG, isStudent);

                                //Get datasnapshot at your "users" root node
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("studentInfo");
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //Get map of users in datasnapshot
                                                listStudents((Map<String, Object>) dataSnapshot.getValue());
                                                Log.d(TAG, sStud);
                                                if (sStud.contains(isStudent)) {
                                                    startActivity(new Intent(SignInActivity.this.getApplicationContext(), StudentHomeActivity.class));
                                                } else {
                                                    startActivity(new Intent(SignInActivity.this.getApplicationContext(), HomeActivity.class));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                //handle databaseError
                                            }
                                        });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Incorrect email or password.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(SignInActivity.this, "Enter your email and password to sign in.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    //Function to navigate to the Home Screen
    private void toBrowse() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //Function to navigate to the Create Account Screen
    private void toCreateAcc() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    private void listStudents(Map<String, Object> studentInfo) {

        ArrayList<String> students = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : studentInfo.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            students.add((String) singleUser.get("uid"));
        }

        System.out.println(students.toString());
        sStud = students.toString();
    }

    private void promptEmail (){

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Email a recovery link");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(SignInActivity.this).inflate(R.layout.activity_dialog_recover_email, null, false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                m_Text = input.getText().toString();
                sendPwResetEmail(m_Text);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();

    }

    private void sendPwResetEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

}

