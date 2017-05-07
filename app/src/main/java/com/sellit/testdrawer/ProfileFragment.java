package com.sellit.testdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ProfileFragment extends Fragment {

    View myView;

    RecyclerView recView;
    RecyclerView recVewSold;

    FloatingActionButton toSettings;

    TextView firstName;
    TextView state;
    TextView city;

    String uid;

    UserInfo userPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_profile,container,false);
        Bundle args = getArguments();
        uid = args.getString("uid");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        //Gets the users info from the database to populate all of the fields on this screen.
        ValueEventListener listener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                userPage = dataSnapshot.getValue(UserInfo.class);
                firstName.setText(userPage.fullName);
                city.setText(userPage.city + ", ");
                state.setText(userPage.state);
                setupRecView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("userInfo").child(uid).addListenerForSingleValueEvent(listener);
        settings();
        TabHost host = (TabHost)myView.findViewById(R.id.hostTab);
        host.setup();

        //Items for Sale Tab
        TabHost.TabSpec spec = host.newTabSpec("Selling");
        spec.setContent(R.id.itemsForSale);
        spec.setIndicator("Selling");
        host.addTab(spec);

        //Sold Items Tab
        spec = host.newTabSpec("Sold");
        spec.setContent(R.id.soldItems);
        spec.setIndicator("Sold");
        host.addTab(spec);

        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);

        return myView;
    }

    private void setupRecView()
    {
        getItemsForSale();
        getSoldItems();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        recView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recVewSold.setLayoutManager(manager2);
        recVewSold.setNestedScrollingEnabled(false);
        recVewSold.setItemAnimator(new DefaultItemAnimator());
    }

    private void getItemsForSale()
    {
        final ArrayList<Item> listItems = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("items");
        mRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> items = (HashMap<String, Object>) dataSnapshot.getValue();
                if(items==null)
                {
                    return;
                }
                Set keys = items.keySet();
                Object[] itemList = items.values().toArray();
                final ItemListAdapter adapter = new ItemListAdapter(listItems, getActivity());
                for(int i = 0; i < itemList.length;i++)
                {
                    Object item = itemList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String, Object> itemMap = (HashMap<String, Object>) item;
                    final Item itemTemp = new Item();
                    itemTemp.description = (String) itemMap.remove("description");
                    Log.d(Item.TAG, "description: " + itemTemp.description);
                    itemTemp.Key = Key;
                    itemTemp.name = (String) itemMap.remove("name");
                    itemTemp.price = (String) itemMap.remove("price");
                    itemTemp.rating = ((Long) itemMap.remove("rating")).intValue();
                    itemTemp.uid = (String) itemMap.remove("uid");
                    itemTemp.isSold = (boolean) itemMap.remove("isSold");
                    if(itemTemp.isSold){continue;}
                    String path = "gs://nationals-master.appspot.com";
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                    StorageReference sRef = storageReference.child("images/items/"+Key+".png");
                    sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            itemTemp.image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            listItems.add(itemTemp);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }

                recView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getSoldItems()
    {
        final ArrayList<Item> listItems = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("soldItems");
        mRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> items = (HashMap<String, Object>) dataSnapshot.getValue();
                if(items==null)
                {
                    return;
                }
                Set keys = items.keySet();
                Object[] itemList = items.values().toArray();
                final ItemListAdapter adapter = new ItemListAdapter(listItems, getActivity());
                for(int i = 0; i < itemList.length;i++)
                {
                    Object item = itemList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String, Object> itemMap = (HashMap<String, Object>) item;
                    final Item itemTemp = new Item();
                    itemTemp.description = (String) itemMap.remove("description");
                    Log.d(Item.TAG, "description: " + itemTemp.description);
                    itemTemp.Key = Key;
                    itemTemp.name = (String) itemMap.remove("name");
                    itemTemp.price = (String) itemMap.remove("price");
                    itemTemp.rating = ((Long) itemMap.remove("rating")).intValue();
                    itemTemp.uid = (String) itemMap.remove("uid");
                    itemTemp.isSold = (boolean) itemMap.remove("isSold");
                    if(!itemTemp.isSold){continue;}
                    String path = "gs://nationals-master.appspot.com";
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                    StorageReference sRef = storageReference.child("images/items/"+Key+".png");
                    sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            itemTemp.image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            listItems.add(itemTemp);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }

                recVewSold.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        city = (TextView) view.findViewById(R.id.profileCity);
        state = (TextView) view.findViewById(R.id.profileState);
        firstName = (TextView) view.findViewById(R.id.profileFirstName);
        recView = (RecyclerView) view.findViewById(R.id.profileRecView);
        recVewSold = (RecyclerView) view.findViewById(R.id.profileRecViewSold);


    }

    private void settings(){
        toSettings = (FloatingActionButton) myView.findViewById(R.id.toSettings);
        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettingsMeth();
            }
        });
    }

    private void toSettingsMeth(){
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        startActivity(intent);
    }
}