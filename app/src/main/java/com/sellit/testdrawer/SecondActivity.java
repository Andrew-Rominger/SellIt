package com.sellit.testdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


public class SecondActivity extends AppCompatActivity{

    Button toSignIn;
    Button attributesBtn;
    ImageButton saveBtn;
    Button addGoal;

    Spinner stateSpinner;

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        save();
        signOut();
        attributes();
        goals();
        String[] states = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        stateSpinner = (Spinner) findViewById(R.id.spinnerSettings);
        stateSpinner.setAdapter(adapter);

        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        city = (EditText) findViewById(R.id.cityInput);

        addGoal = (Button) findViewById(R.id.addGoalBtn);
    }
    private void signOut(){
        toSignIn = (Button) findViewById(R.id.signOutBtn);
        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignOut();
            }
        });
    }

    private void save(){
        saveBtn = (ImageButton)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void attributes(){
        attributesBtn = (Button)findViewById(R.id.attributionsBtn);
        attributesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAttributes();
            }
        });
    }
    private void goals(){
        addGoal = (Button) findViewById(R.id.addGoalBtn);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGoals();
            }
        });
    }
    private void toSignOut(){
        Intent intent = new Intent(this, FourthLayout.class);
        startActivity(intent);
    }

    private void toAttributes(){
        Intent intent = new Intent(this, AttributesActivity.class);
        startActivity(intent);
    }

    private void toGoals(){
        Intent intent = new Intent(this, GoalsActivity.class);
        startActivity(intent);
    }

    //Save inputted data to firebase
    private void saveData(){

    }
}
