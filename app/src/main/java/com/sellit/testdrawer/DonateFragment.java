package com.sellit.testdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class DonateFragment extends Fragment {

    RecyclerView recView;
    View myView;
    String TAG = DonateFragment.class.getSimpleName();

    FloatingActionButton createDonation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_donate,container,false);
        newDonation();
        createDonation = (FloatingActionButton) myView.findViewById(R.id.createDonation);
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.donationList
                        , new ListDonationsFragment())
                .commit();

        return myView;
    }
    private void newDonation(){
        createDonation = (FloatingActionButton) myView.findViewById(R.id.createDonation);
        createDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNewDonation();
            }
        });
    }
    private void toNewDonation(){
        Intent intent = new Intent(getActivity(), DonationActivity.class);
        startActivity(intent);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }

    public void setUpRec(View view) {
        getDonatedItems();
        LinearLayoutManager manager=new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        recView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getDonatedItems() {
        final ArrayList<DonatedItem> list = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("donatedItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String,Object> donations = (HashMap<String, Object>) dataSnapshot.getValue();
                if (donations == null) {
                    return;
                }
                Set keys = donations.keySet();
                Object[] donationList = donations.values().toArray();
                final DonatedItemListAdapter adapter = new DonatedItemListAdapter(list,DonateFragment.this.getActivity());
                for(int i = 0; i<donationList.length;i++){
                    Object donation = donationList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String,Object> itemMap = (HashMap<String, Object>) donation;
                    final DonatedItem donatedItem = new DonatedItem();
                    donatedItem.donatedDescription = (String) itemMap.remove("description");
                    donatedItem.donationKey = Key;
                    donatedItem.donatedName = (String) itemMap.remove("name");
                    donatedItem.donatedPrice = (String) itemMap.remove("price");
                    donatedItem.uid = (String) itemMap.remove("uid");
                    donatedItem.rating = ((Long)itemMap.remove("rating")).intValue();
                    String path = "gs://nationals-master.appspot.com";
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                    Log.d(TAG, path);
                    StorageReference sRef = storageReference.child("images/items/"+Key+".png");
                    sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            Log.d(TAG, "Num Bytes: " + bytes.length);
                            donatedItem.image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            list.add(donatedItem);
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

    private class DonatedItemListAdapter extends RecyclerView.Adapter<DonatedItemListAdapter.DonatedItemViewHolder>
    {
        ArrayList<DonatedItem> list;
        Context context;
        LayoutInflater inflator;

        public DonatedItemListAdapter(ArrayList<DonatedItem> list, Activity activity)
        {
            this.list = list;
            this.context = activity.getApplicationContext();
            inflator=LayoutInflater.from(context);
        }

        @Override
        public DonatedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DonatedItemViewHolder(inflator.inflate(R.layout.item_in_list,parent,false));
        }

        @Override
        public void onBindViewHolder(DonatedItemViewHolder holder, int position) {
            holder.setData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class DonatedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            View itemView;
            ImageView itemImage;
            TextView Title;
            TextView Description;
            TextView Price;
            FrameLayout wrapper;
            DonatedItem donation;
            public DonatedItemViewHolder(View itemView) {
                super(itemView);
                Log.d(TAG, "ItemViewHolder Contructor");
                this.itemView = itemView;
                itemImage = (ImageView) itemView.findViewById(R.id.DI_Thumbnail);
                Title = (TextView) itemView.findViewById(R.id.DI_ItemName);
                Description = (TextView) itemView.findViewById(R.id.DI_ItemDescription);
                Price = (TextView) itemView.findViewById(R.id.DI_ItemPrice);
                wrapper = (FrameLayout) itemView.findViewById(R.id.DI_Wrapper);
                wrapper.setOnClickListener(this);

            }

            public void setData(DonatedItem donatedItem)
            {
                this.donation = donatedItem;
                Log.d(TAG, "setData");
                Title.setText(donation.donatedName);
                Description.setText(donation.donatedDescription);
                Price.setText(donation.donatedPrice);
                itemImage.setImageDrawable(donation.image);
            }

            @Override
            public void onClick(View v)
            {

            }
        }
    }
}
