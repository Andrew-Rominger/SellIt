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
    public String donatedTo;
    public boolean isSold;

    public Item(String name, String price, String description, int rating, String uid, String donatedTo, boolean isSold) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.uid = uid;
        this.isSold = isSold;
        this.donatedTo = donatedTo;
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
        result.put("donatedTo", donatedTo);
        return result;
    }


}
