package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class SecondFragment extends Fragment{

    View myView;

    Button toSignIn;
    Button saveBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout,container,false);
        save();
        signOut();
        return myView;
    }
    private void signOut(){
        toSignIn = (Button) myView.findViewById(R.id.signOutBtn);
        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignIn();
            }
        });
    }

    private void save(){
        saveBtn = (Button) myView.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    private void toSignIn(){
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }

    private void saveData(){

    }
}
