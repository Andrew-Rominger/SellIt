package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class ProfileFragment extends Fragment {

    View myView;

    FloatingActionButton toSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_profile,container,false);

        settings();
        TabHost host = (TabHost)myView.findViewById(R.id.hostTab);
        host.setup();

        //Items for Sale Tab
        TabHost.TabSpec spec = host.newTabSpec("Selling");
        spec.setContent(R.id.itemsForSale);
        spec.setIndicator("Selling");
        host.addTab(spec);

        //Sold Items Tab
        spec = host.newTabSpec("Sold");
        spec.setContent(R.id.soldItems);
        spec.setIndicator("Sold");
        host.addTab(spec);

        //Donations Tab
        spec = host.newTabSpec("Donations");
        spec.setContent(R.id.donations);
        spec.setIndicator("Donations");
        host.addTab(spec);

        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);

        return myView;
    }

    private void settings(){
        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);
        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettingsMeth();
            }
        });
    }

    private void toSettingsMeth(){
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        startActivity(intent);
    }
}