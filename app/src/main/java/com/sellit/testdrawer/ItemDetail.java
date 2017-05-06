package com.sellit.testdrawer;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemDetail extends AppCompatActivity {
    String currentUserName;
    String Key;
    String TAG = ItemDetail.class.getSimpleName();
    String itemViewing;
    String uid;
    String userPostedEmail;
    String userPostedName;
    String itemName;
    DatabaseReference dRef;
    Item item;
    UserInfo userInfo;
    Button contact;
    Boolean isCurrentUser;

    TextView Name;
    TextView Description;
    TextView Rating;
    TextView ItemPrice;
    TextView Location;
    Button isOwner;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        image = (ImageView) findViewById(R.id.itemImageDetail);
        Name = (TextView) findViewById(R.id.itemNameDetail);
        Description = (TextView) findViewById(R.id.ID_ItemDescription);
        ItemPrice = (TextView) findViewById(R.id.ID_ItemPrice);
        Location = (TextView) findViewById(R.id.ID_location);
        Key = getIntent().getStringExtra("Key");
        contact = (Button) findViewById(R.id.contactItemDetailButton);
        isOwner = (Button) findViewById(R.id.isOwner);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{userPostedEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding: " + itemName);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello " + userPostedName + ", I am interested in buying the item: " + itemName + ". How about we stay in contact?\n From, \n" + currentUserName);
                startActivity(intent.createChooser(intent, "Choose An Email App:"));
            }
        });

        Log.d(TAG, "Key: " + Key);
        dRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference itemRef = dRef.child("items/" + Key);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);
                itemName = item.name;
                uid = item.uid;
                Name.setText(item.name);
                Description.setText(item.description);
                ItemPrice.setText("$" + item.price);
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(uid)
                        && isOwner.getText().toString().equals("Mark Item As Sold")) {
                    Log.d(TAG, "Found As Owner");
                    isOwner.setVisibility(View.VISIBLE);
                    isOwner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Item item1 = new Item(item.name, item.price,
                                    item.description, item.rating, item.uid, true);

                            dRef.child("items").child(Key).removeValue();

                            DatabaseReference itemRef = dRef.child("soldItems/" + Key);
                            itemRef.updateChildren(item1.toMap());
                            Log.d(TAG, "ID: " + Key);
                            Toast.makeText(ItemDetail.this, "Item Marked As Sold", Toast.LENGTH_SHORT).show();
                            isOwner.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(ItemDetail.this, SellerHomeActivity.class));
                        }
                    });
                }
                DatabaseReference uRef = FirebaseDatabase.getInstance().getReference("userInfo");
                ValueEventListener listener_user = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfo u = dataSnapshot.getValue(UserInfo.class);
                        String locationText = u.city + ", " + u.state;

                        Location.setText(locationText);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                uRef.child(item.uid).addListenerForSingleValueEvent(listener_user);

                String path = "gs://nationals-master.appspot.com";
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                Log.d(TAG, path);
                final StorageReference sRef = storageReference.child("images/items/" + Key + ".png");
                sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        };
        itemRef.addListenerForSingleValueEvent(listener);

        DatabaseReference uRef = FirebaseDatabase.getInstance().getReference();

        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPostedEmail = dataSnapshot.child("userInfo").child(uid).child("email").getValue().toString();
                userPostedName = dataSnapshot.child("userInfo").child(uid).child("userName").getValue().toString();
                currentUserName = dataSnapshot.child("userInfo").child(uid).child("userName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        uRef.addListenerForSingleValueEvent(listener1);

        FragmentManager FM = getFragmentManager();
        android.app.FragmentTransaction transaction = FM.beginTransaction();
        Bundle args = new Bundle();
        args.putString("postID", Key);
        CommentFragment CF = new CommentFragment();
        CF.setArguments(args);
        transaction.replace(R.id.commentFragmentHolder, CF);
        transaction.commit();
    }
}
