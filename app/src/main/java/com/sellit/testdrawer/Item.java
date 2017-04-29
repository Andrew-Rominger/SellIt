package com.sellit.testdrawer;

import android.graphics.drawable.Drawable;

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


}
