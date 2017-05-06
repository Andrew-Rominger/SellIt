package com.sellit.testdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CFCI on 5/1/2017.
 */

public class EditItemActivity extends AppCompatActivity {

    Button saveBtnEdit;
    Button takePhotoEdit;
    Spinner itemTypeEdit;
    String[] categories;
    String selected, spinner_item;
    Boolean isMarshmallowOrHigher;
    Bitmap myBitmap;
    int itemType_position;
    String pictureImagePath;
    EditText itemNameEdit;
    EditText itemPriceEdit;
    EditText itemDescriptionEdit;
    RatingBar itemConditionEdit;
    ImageView imagePicEdit;
    Spinner stateSpinnerEdit;

    String TAG = EditItemActivity.class.getSimpleName();
    String[] permsRequested = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);

        //Initialize variables on activity
        isMarshmallowOrHigher = isMarshmallowOrHigher();
        saveBtnEdit = (Button) findViewById(R.id.saveBtnEdit);
        takePhotoEdit = (Button) findViewById(R.id.takePicBtnEdit);
        itemTypeEdit = (Spinner) findViewById(R.id.itemTypeEdit);
        itemNameEdit = (EditText) findViewById(R.id.itemNameEdit);
        itemPriceEdit = (EditText) findViewById(R.id.itemPriceEdit);
        itemDescriptionEdit = (EditText) findViewById(R.id.itemDescriptionEdit);
        itemConditionEdit = (RatingBar) findViewById(R.id.itemConditionEdit);
        imagePicEdit = (ImageView) findViewById(R.id.photoImgEdit);
        String[] states = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        stateSpinnerEdit = (Spinner) findViewById(R.id.addItemStateSpinnerEdit);
        stateSpinnerEdit.setAdapter(adapter);

        //In current version, sets the spinner on screen to the elements in the strings file at: res -> values -> strings.xml
        String myString = "Item Category";
        categories = getResources().getStringArray(R.array.spinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(EditItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        itemType_position = ad.getPosition(myString);
        itemTypeEdit.setAdapter(ad);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemTypeEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selected = itemTypeEdit.getSelectedItem().toString();
                if (!selected.equals("Thing"))
                    spinner_item = selected;
                System.out.println(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //On button start camera
        takePhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserPerms();
                // This was before checking for permissions to read and write. Call later. openBackCamera(1, EditItemActivity.this);
            }
        });

        saveBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!itemNameEdit.getText().toString().isEmpty() && !itemPriceEdit.getText().toString().isEmpty() && !itemDescriptionEdit.getText().toString().isEmpty() && itemConditionEdit.getRating() != 0) {
                    Item item = new Item(itemNameEdit.getText().toString(), itemPriceEdit.getText().toString(), itemDescriptionEdit.getText().toString(), (int) itemConditionEdit.getRating(), FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    String key = mRef.push().getKey();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/items/" + key, item.toMap());
                    mRef.updateChildren(childUpdates);
                    Log.d(TAG, "ID: " + key);
                    StorageReference sRef = FirebaseStorage.getInstance().getReference().child("images/items/" + key + ".png");
                    ByteArrayOutputStream OS = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, OS);
                    byte[] data = OS.toByteArray();
                    UploadTask UT = sRef.putBytes(data);
                    UT.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Could not save Image. " + e.getLocalizedMessage());
                        }
                    });
                    UT.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditItemActivity.this, "Saved Posting", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(EditItemActivity.this, HomeActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(EditItemActivity.this, "All fields must be filled out to save.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });


    }

    //Requests to use the write to storage, and read to storage. Checks to see if the request has already been made.

    private boolean isMarshmallowOrHigher () {
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    private void checkUserPerms () {
        int permissionCheck = ContextCompat.checkSelfPermission(EditItemActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(EditItemActivity.this, permissionCheck + "Granted Permission", Toast.LENGTH_LONG);
            openBackCamera(1, EditItemActivity.this);
        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EditItemActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                isMarshmallowOrHigher) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditItemActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditItemActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    //On event permission is allowed.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // storage-related task you need to do.
                    openBackCamera(1, EditItemActivity.this);
                } else {
                    // do nothing
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //Intent to start camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //gets picture from camera and handles it
        File imgFile = new File(pictureImagePath);
        Log.i("PRINT IMGFILE", imgFile.getAbsolutePath());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
        myBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth() / 2, myBitmap.getHeight() / 2, true);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(pictureImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                myBitmap = rotateImage(myBitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                myBitmap = rotateImage(myBitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                myBitmap = rotateImage(myBitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:

            default:
                break;
        }

        myBitmap = getResizedBitmap(myBitmap, 300);
        imagePicEdit.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));


    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void openBackCamera(int numCode, Context context)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.i("path1",storageDir.getAbsolutePath());
        Log.i("path2", imageFileName);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        Log.i("path3", pictureImagePath);
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, numCode);
    }
}
