package com.sellit.testdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

public class AddItemActivity extends AppCompatActivity {

    Button submitItem;
    Button takePhoto;
    Spinner itemType;
    String[] categories;
    String selected, spinner_item;
    int itemType_position;
    EditText itemName;
    EditText itemPrice;
    EditText itemDescription;
    RatingBar itemCondtion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        //Initialize variables on activity
        submitItem = (Button) findViewById(R.id.submitBtn);
        takePhoto = (Button) findViewById(R.id.takePicBtn);
        itemType = (Spinner) findViewById(R.id.itemType);
        itemName = (EditText) findViewById(R.id.itemNameInput);
        itemPrice = (EditText) findViewById(R.id.itemPriceInput);
        itemDescription = (EditText) findViewById(R.id.itemDescriptionInput);
        itemCondtion = (RatingBar) findViewById(R.id.itemCondition);

        //In current version, sets the spinner on screen to the elements in the strings file at: res -> values -> strings.xml
        String myString = "Item Category";
        categories = getResources().getStringArray(R.array.spinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        itemType_position = ad.getPosition(myString);
        itemType.setAdapter(ad);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selected = itemType.getSelectedItem().toString();
                if (!selected.equals("Thing"))
                    spinner_item = selected;
                System.out.println(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


}
