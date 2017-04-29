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

public class Item
{

    static String TAG = Item.class.getSimpleName();
    public String name;
    public String price;
    public String description;
    public int rating;
    public Drawable image;
    public String uid;
    public String Key;
    public boolean isSold;

    public Item(String name, String price, String description, int rating, String uid, boolean isSold) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.uid = uid;
        this.isSold = isSold;

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
        result.put("isSold",isSold);
        return result;
    }


}
