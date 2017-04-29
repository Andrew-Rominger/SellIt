package com.sellit.testdrawer;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class ItemDetail extends AppCompatActivity
{
    String Key;
    String TAG = ItemDetail.class.getSimpleName();
    DatabaseReference dRef;
    Item item;

    ImageView image;
    TextView Name;
    TextView Description;
    TextView Rating;
    TextView ItemPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Name = (TextView) findViewById(R.id.ID_ItemName);
        Description = (TextView) findViewById(R.id.ID_ItemDescription);
        ItemPrice = (TextView) findViewById(R.id.ID_ItemPrice);
        image = (ImageView) findViewById(R.id.ID_Image);
        Key = getIntent().getStringExtra("Key");
        Log.d(TAG, "Key: " + Key);
        dRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference itemRef = dRef.child("items/"+Key);

        ValueEventListener listener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                item =  dataSnapshot.getValue(Item.class);
                Name.setText(item.name);
                Description.setText(item.description);
                ItemPrice.setText(item.price);


                String path = "gs://nationals-master.appspot.com";
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                Log.d(TAG, path);
                StorageReference sRef = storageReference.child("images/items/"+Key+".png");
                sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>()
                {
                   @Override
                   public void onSuccess(byte[] bytes) {
                       Log.d(TAG, "Num Bytes: " + bytes.length);
                       item.image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                       image.setImageDrawable(item.image);
                   }
                });
                Log.d(TAG, "description: " + item.description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, databaseError.getDetails());
            }
        };
        itemRef.addListenerForSingleValueEvent(listener);
    }
}
