package com.sellit.testdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/29/2017.
 */

public class ListAllFragment extends Fragment
{
    RecyclerView recView;
    String TAG = ListAllFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_all_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        recView = (RecyclerView) view.findViewById(R.id.listAllRecView);
        setupRec(view);
        super.onViewCreated(view, savedInstanceState);
    }
    private void setupRec(View view)
    {

        getItems();
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(manager);
        recView.setItemAnimator(new DefaultItemAnimator());

    }
    public void getItems()
    {
        final ArrayList<Item> listItems = new ArrayList<>();
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
                    listItems.add(i);
                }
                Log.d(TAG, "Num Items: " + listItems.size());
                ItemListAdapter adapter = new ItemListAdapter(listItems,ListAllFragment.this.getActivity());
                recView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return;
    }
}
