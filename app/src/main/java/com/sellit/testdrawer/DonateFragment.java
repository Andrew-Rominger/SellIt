package com.sellit.testdrawer;

        import android.app.Fragment;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TabHost;

public class DonateFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_donate,container,false);

        TabHost host = (TabHost)myView.findViewById(R.id.tabHost);
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

        return myView;
    }

}