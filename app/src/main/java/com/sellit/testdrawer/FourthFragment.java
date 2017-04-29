package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class FourthFragment extends Fragment{

    View myView;
    Button toHome;
    Button toSignIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fourth_layout,container,false);
        setUp();
        return myView;
    }

    private void setUp(){
        toHome = (Button) myView.findViewById(R.id.noBtn);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHome();
            }
        });

        toSignIn = (Button) myView.findViewById(R.id.yesBtn);
        toSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toSignIn();
            }
        });
    }
    private void toHome(){
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private void toSignIn(){
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }
}
