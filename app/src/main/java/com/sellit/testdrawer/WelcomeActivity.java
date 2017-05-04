package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class WelcomeActivity extends AppCompatActivity {

    //Variables for the buttons and Edit Text Boxes
    Button buySell;
    Button student;
    Button toSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Variables for the inputted text
        buySell = (Button) findViewById(R.id.buyerBtn);
        student = (Button) findViewById(R.id.studentBtn);
        toSignIn = (Button) findViewById(R.id.toSignInBtn);

        //On Click Listener for the Continue Button
        buySell = (Button) findViewById(R.id.buyerBtn);
        buySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });
        student = (Button) findViewById(R.id.studentBtn);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentSignUp();
            }
        });

        //On Click listener for the Sign In Button
        toSignIn = (Button) findViewById(R.id.toSignInBtn);
        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignInBtn();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void studentSignUp() {
        Intent intent = new Intent(this, StudentSignUp.class);
        startActivity(intent);
    }

    private void userSignUp() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    //Function for clicking the Sign In Button and navigating to it
    private void toSignInBtn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}