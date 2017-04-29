package com.sellit.testdrawer;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.TabHost;

public class DonateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Incoming Tab
        TabHost.TabSpec spec = host.newTabSpec("Incoming");
        spec.setContent(R.id.Incoming);
        spec.setIndicator("Incoming");
        host.addTab(spec);

        //Outgoing Tab
        spec = host.newTabSpec("Outgoing");
        spec.setContent(R.id.Outgoing);
        spec.setIndicator("Outgoing");
        host.addTab(spec);
    }

}