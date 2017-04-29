package com.sellit.testdrawer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */

public class Item extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itempage);

        setToDonate();
    }

    static String TAG = Item.class.getSimpleName();
    public String name;
    public String price;
    public String description;
    public int rating;
    public Drawable image;
    public String uid;
    public String Key;
    public FloatingActionButton toDonate;

    public Item(String name, String price, String description, int rating, String uid) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.uid = uid;
    }

    public Item() {
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("price", price);
        result.put("description", description);
        result.put("rating", rating);
        return result;
    }

    private void setToDonate(){
        toDonate = (FloatingActionButton) findViewById(R.id.toDonate);
        Intent intent = new Intent(this, DonateFragment.class);
        startActivity(intent);
    }
}
