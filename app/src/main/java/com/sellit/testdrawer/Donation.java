package com.sellit.testdrawer;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */

public class Donation
{

    static String TAG = Donation.class.getSimpleName();
    public String donatedName;
    public String donatedPrice;
    public String donatedDescription;
    public int rating;
    public Drawable image;
    public String uid;
    public String donationKey;
    public boolean isSold;

    public Donation(String name, String price, String description, int rating, String uid, boolean isSold) {
        this.donatedName = name;
        this.donatedPrice = price;
        this.donatedDescription = description;
        this.rating = rating;
        this.uid = uid;
        this.isSold = isSold;

    }

    public Donation() {
    }

    public Map<String, Object> donationToMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", donatedName);
        result.put("price", "Donated");
        result.put("description", donatedDescription);
        result.put("rating", rating);
        result.put("isSold",isSold);
        return result;
    }


}
