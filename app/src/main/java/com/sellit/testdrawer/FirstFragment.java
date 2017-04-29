package com.sellit.testdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 2524904 on 4/5/2017.
 */

public class FirstFragment extends Fragment{

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout,container,false);
        return myView;
    }

    class Item {
        String Name;
        String Description;
        String Category;
        int Condition;
        int Price;

        public Item (String Name, String Description, String Category, int Condition, int Price) {
            this.Name = Name;
            this.Description = Description;
            this.Category = Category;
            this.Condition = Condition;
            this.Price = Price;
        }

        //private Item getItem(String reference) {


            //return item;
        //}

    }

    Item thingIWantToSell = new Item("Thing I want to sell", "Good Description", "Electronics", 3, 80);

    public void thing
    Toast.makeText(SignInActivity.this, Name.thingIWantToSell, Toast.LENGTH_LONG).show();


}
