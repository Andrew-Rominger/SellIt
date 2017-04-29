package com.sellit.testdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class DonateFragment extends Fragment {

    RecyclerView recView;
    View myView;
    String TAG = DonateFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_donate,container,false);

        TabHost host = (TabHost)myView.findViewById(R.id.tabHost);
        host.setup();

        //Incoming Tab
        TabHost.TabSpec spec = host.newTabSpec("Incoming");
        spec.setContent(R.id.Incoming);
        spec.setIndicator("Incoming");
        host.addTab(spec);

        //Outgoing Tab
        spec = host.newTabSpec("Outgoing");
        spec.setContent(R.id.Outgoing);
        spec.setIndicator("Outgoing");
        host.addTab(spec);

        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recView= (RecyclerView) view.findViewById(R.id.donationIncomingRecView);

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
                HashMap<String,Object> items = (HashMap<String, Object>) dataSnapshot.getValue();
                if (items == null) {
                    return;
                }
                Set keys = items.keySet();
                Object[] itemList = items.values().toArray();
                final DonatedItemListAdapter adapter = new DonatedItemListAdapter(list,DonateFragment.this.getActivity());
                for(int i = 0; i<itemList.length;i++){
                    Object item = itemList[i];
                    String Key = (String) keys.toArray()[i];
                    HashMap<String,Object> itemMap = (HashMap<String, Object>) item;
                    final DonatedItem donatedItem = new DonatedItem();
                    donatedItem.description = (String) itemMap.remove("description");
                    donatedItem.Key = Key;
                    donatedItem.name = (String) itemMap.remove("name");
                    donatedItem.price = (String) itemMap.remove("price");
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
            DonatedItem item;
            public DonatedItemViewHolder(View itemView) {
                super(itemView);
                Log.d(TAG, "ItemViewHolder Contructor");
                this.itemView = itemView;
                itemImage = (ImageView) itemView.findViewById(R.id.IIL_Thumbnail);
                Title = (TextView) itemView.findViewById(R.id.IIL_ItemName);
                Description = (TextView) itemView.findViewById(R.id.IIL_ItemDescription);
                Price = (TextView) itemView.findViewById(R.id.IIL_ItemPrice);
                wrapper = (FrameLayout) itemView.findViewById(R.id.IIL_Wrapper);
                wrapper.setOnClickListener(this);

            }

            public void setData(DonatedItem donatedItem)
            {
                this.item = donatedItem;
                Log.d(TAG, "setData");
                Title.setText(item.name);
                Description.setText(item.description);
                Price.setText(item.price);
                itemImage.setImageDrawable(item.image);
            }

            @Override
            public void onClick(View v)
            {

            }
        }
    }
}
