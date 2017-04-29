package com.sellit.testdrawer;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */

public class Item
{
    static String TAG = Item.class.getSimpleName();
    public String Name;
    public String Price;
    public String Description;
    public int Rating;
    public Drawable image;
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

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", UID);
        result.put("name", Name);
        result.put("price", Price);
        result.put("description", Description);
        result.put("rating", Rating);

        return result;
    }

    public static ArrayList<Item> getAllItems()
    {
        final ArrayList<Item> itemsList = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("items").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> items = (HashMap<String, Object>) dataSnapshot.getValue();
                for(Object item : items.values())
                {
                    HashMap<String, Object> itemMap = (HashMap<String, Object>) item;

                    Item i = new Item();
                    i.Description = (String) itemMap.remove("description");
                    Log.d(Item.TAG, "Description: " + i.Description );
                    i.Name = (String) itemMap.remove("name");
                    i.Price = (String) itemMap.remove("price");
                    i.Rating = ((Long) itemMap.remove("rating")).intValue();
                    i.UID = (String) itemMap.remove("uid");
                    itemsList.add(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return itemsList;
    }
}
