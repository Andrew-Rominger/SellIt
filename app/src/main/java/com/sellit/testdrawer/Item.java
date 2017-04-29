package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */

public class Item
{
    public String Name;
    public String Price;
    public String Description;
    public int Rating;
    public String UID;
    public Item(String name, String price, String description, int rating, String UID) {
        Name = name;
        Price = price;
        Description = description;
        Rating = rating;
        this.UID = UID;
    }

    public Item() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", UID);
        result.put("name", Name);
        result.put("price", Price);
        result.put("description", Description);
        result.put("rating", Rating);

        return result;
    }
}
