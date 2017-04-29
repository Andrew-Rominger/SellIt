package com.sellit.testdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/29/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>
{
    ArrayList<Item> itemsList;
    Context context;
    LayoutInflater inflater;
    String TAG = ItemListAdapter.class.getSimpleName();
    public ItemListAdapter(ArrayList<Item> items, Context c)
    {
        Log.d(TAG, "Adapter Constructor");
        itemsList = items;
        this.context = c;
        inflater = LayoutInflater.from(c);
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

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        ImageView itemImage;
        TextView Title;
        TextView Description;
        TextView Price;

        public ItemViewHolder(View itemView)
        {

            super(itemView);
            Log.d(TAG, "ItemViewHolder Contructor");
            this.itemView = itemView;
            itemImage = (ImageView) itemView.findViewById(R.id.IIL_Thumbnail);
            Title = (TextView) itemView.findViewById(R.id.IIL_ItemName);
            Description = (TextView) itemView.findViewById(R.id.IIL_ItemDescription);
            Price = (TextView) itemView.findViewById(R.id.IIL_ItemPrice);
        }

        public void setData(Item item)
        {
            Log.d(TAG, "setData");
            Title.setText(item.Name);
            Description.setText(item.Description);
            Price.setText(item.Price);
            itemImage.setImageDrawable(item.image);
        }
    }
}
