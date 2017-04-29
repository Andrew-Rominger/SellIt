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

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>
{
    ArrayList<Item> itemsList;
    Context context;
    LayoutInflater inflater;
    Activity A;
    String TAG = ItemListAdapter.class.getSimpleName();
    public ItemListAdapter(ArrayList<Item> items, Activity A)
    {
        Log.d(TAG, "Adapter Constructor");
        itemsList = items;
        this.A = A;
        this.context = this.A.getApplicationContext();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.d(TAG, "OnCreateViewHolder");
        return new ItemListAdapter.ItemViewHolder(inflater.inflate(R.layout.item_in_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        Log.d(TAG, "BindViewHolder");
        holder.setData(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        ImageView itemImage;
        TextView Title;
        TextView Description;
        TextView Price;
        FrameLayout wrapper;
        Item item;
        public ItemViewHolder(View itemView)
        {
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

        public void setData(Item item)
        {
            this.item = item;
            Log.d(TAG, "setData");
            Title.setText(item.name);
            Description.setText(item.description);
            Price.setText(item.price);
            itemImage.setImageDrawable(item.image);
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(v.getContext(), ItemDetail.class);
            i.putExtra("Key", item.Key);
            v.getContext().startActivity(i);
        }
    }
}
