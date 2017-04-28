package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class FourthLayout extends AppCompatActivity {

    Button toHome;
    Button toSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_layout);

        toHome = (Button) findViewById(R.id.noBtn);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHome();
            }
        });

        toSignIn = (Button) findViewById(R.id.yesBtn);
        toSignIn.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               toSignIn();
           }
        });
    }

    private void toHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void toSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
