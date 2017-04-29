package com.sellit.testdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class GoalsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        TabHost host = (TabHost)findViewById(R.id.hostTab);
        host.setup();

        //Add Goals Tab
        TabHost.TabSpec spec = host.newTabSpec("Add Goals");
        spec.setContent(R.id.addGoals);
        spec.setIndicator("Add Goals");
        host.addTab(spec);

        //Current Goals Tab
        spec = host.newTabSpec("Current Goals");
        spec.setContent(R.id.currentGoals);
        spec.setIndicator("Current Goals");
        host.addTab(spec);

    }
}