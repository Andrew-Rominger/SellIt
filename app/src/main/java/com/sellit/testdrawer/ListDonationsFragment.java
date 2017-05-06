package com.sellit.testdrawer;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

/**
 * Created by Andrew on 4/29/2017.
 */

public class ListDonationsFragment extends Fragment
{

    RecyclerView recView;
    String TAG = ListDonationsFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_donations_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        recView = (RecyclerView) view.findViewById(R.id.donationIncomingRecView);
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
        final ArrayList<Donation> donationItems = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> donations = (HashMap<String, Object>) dataSnapshot.getValue();
                if(donations==null)
                {
                    return;
                }
                Set keys = donations.keySet();
                Object[] itemList = donations.values().toArray();
                final DonationListAdapter adapter = new DonationListAdapter(donationItems, getActivity());
                for(int i = 0; i < itemList.length;i++)
                {
                    Object item = itemList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String, Object> itemMap = (HashMap<String, Object>) item;
                    final Donation donationTemp = new Donation();
                    donationTemp.donatedDescription = (String) itemMap.remove("description");
                    Log.d(Item.TAG, "description: " + donationTemp.donatedDescription);
                    donationTemp.donationKey = Key;
                    donationTemp.donatedName = (String) itemMap.remove("name");
                    donationTemp.donatedPrice = (String) itemMap.remove("price");
                    donationTemp.rating = ((Long) itemMap.remove("rating")).intValue();
                    donationTemp.uid = (String) itemMap.remove("uid");
                    String path = "gs://nationals-master.appspot.com";
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                    Log.d(TAG, path);
                    StorageReference sRef = storageReference.child("images/donations/"+Key+".png");
                    sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            Log.d(TAG, "Num Bytes: " + bytes.length);
                            donationTemp.image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            donationItems.add(donationTemp);
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
                Log.d(TAG, "Num Items: " + donationItems.size());

                recView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return;
    }
}
