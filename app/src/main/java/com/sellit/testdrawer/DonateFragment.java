package com.sellit.testdrawer;

import android.app.Fragment;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class DonateFragment extends Fragment {

package info.androidhive.recyclerview;

    public class donatedItem {
        private String name, descirption, condition, year;

        public string (name) {
        }

        public Movie(String title, String genre, String year) {
            this.title = title;
            this.genre = genre;
            this.year = year;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getUSername() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

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