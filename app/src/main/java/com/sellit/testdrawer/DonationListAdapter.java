package com.sellit.testdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew on 4/29/2017.
 */

public class DonationListAdapter extends RecyclerView.Adapter<DonationListAdapter.ItemViewHolder>
{
    ArrayList<Donation> donationList;
    Context context;
    LayoutInflater inflater;
    Activity B;
    String TAG = DonationListAdapter.class.getSimpleName();
    public DonationListAdapter(ArrayList<Donation> donations, Activity B)
    {
        Log.d(TAG, "Adapter Constructor");
        donationList = donations;
        this.B = B;
        this.context = this.B.getApplicationContext();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.d(TAG, "OnCreateViewHolder");
        return new ItemViewHolder(inflater.inflate(R.layout.item_donated, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        Log.d(TAG, "BindViewHolder");
        holder.setData(donationList.get(position));
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        ImageView itemImage;
        TextView Title;
        TextView Description;
        TextView Price;
        FrameLayout wrapper;
        Donation donation;
        public ItemViewHolder(View itemView)
        {
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

        public void setData(Donation donation)
        {
            this.donation = donation;
            Log.d(TAG, "setData");
            Title.setText(donation.donatedName);
            Description.setText(donation.donatedDescription);
            Price.setText(donation.donatedPrice);
            itemImage.setImageDrawable(donation.image);
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(v.getContext(), ItemDetail.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Key", donation.donationKey);
            v.getContext().startActivity(i);
        }
    }
}
