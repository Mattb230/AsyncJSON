package com.mboydston.bottlerockettest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.net.URI;

/**
 * Displays individual store info. Launched from HomeScreen.java
 */

public class ListItem extends AppCompatActivity {

    public static final String EXTRA_ADDRESS = "com.mboydston.bottlerockettest.extra_address";
    public static final String EXTRA_CITY = "com.mboydston.bottlerockettest.extra_city";
    public static final String EXTRA_NAME = "com.mboydston.bottlerockettest.extra_name";
    public static final String EXTRA_LATITUDE = "com.mboydston.bottlerockettest.extra_latitude";
    public static final String EXTRA_ZIP = "com.mboydston.bottlerockettest.extra_zip";
    public static final String EXTRA_PHONE = "com.mboydston.bottlerockettest.extra_phone";
    public static final String EXTRA_LONGITUDE = "com.mboydston.bottlerockettest.extra_longitude";
    public static final String EXTRA_STOREID = "com.mboydston.bottlerockettest.extra_storeid";
    public static final String EXTRA_STATE = "com.mboydston.bottlerockettest.extra_state";
    public static final String EXTRA_LOGO = "com.mboydston.bottlerockettest.extra_logo";

    private TextView mAddressTextView;
    private TextView mNameTextView;
    private TextView mLatitudeTextView;
    private TextView mPhoneTextView;
    private TextView mLongitudeTextView;
    private TextView mStoreIDTextView;
    private ImageView mLogoImageView;

    private String name;
    private String address;
    private String latitude;
    private String phone;
    private String longitude;
    private String storeID;
    private String fileName;
    private Bitmap logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        logo = null;
        fileName = getIntent().getStringExtra(EXTRA_LOGO);
        try {
            FileInputStream is = this.openFileInput(fileName);
            logo = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLogoImageView = (ImageView)findViewById(R.id.logo_imageView);
        mLogoImageView.setImageBitmap(logo);


        name = getIntent().getStringExtra(EXTRA_NAME);
        mNameTextView = (TextView)findViewById(R.id.name_textView);
        mNameTextView.setText(name);

        address = getIntent().getStringExtra(EXTRA_ADDRESS) + ", "
                + getIntent().getStringExtra(EXTRA_CITY) + ", "
                + getIntent().getStringExtra(EXTRA_STATE) + " "
                + getIntent().getStringExtra(EXTRA_ZIP);
        mAddressTextView = (TextView)findViewById(R.id.address_textView);
        SpannableString content = new SpannableString(address);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mAddressTextView.setTextColor(Color.BLUE);
        mAddressTextView.setText(content);

        mAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + mAddressTextView.getText().toString()));
                startActivity(intent);
            }
        });

        phone = getIntent().getStringExtra(EXTRA_PHONE);
        mPhoneTextView = (TextView)findViewById(R.id.phone_textView);
        content = new SpannableString(phone);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mPhoneTextView.setTextColor(Color.BLUE);
        mPhoneTextView.setText(content);


        //on click listener for phone number
        mPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = mPhoneTextView.getText().toString().replace("-", "");
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone_number));
                startActivity(dialIntent);
            }
        });


        storeID =  getIntent().getStringExtra(EXTRA_STOREID);
        mStoreIDTextView = (TextView)findViewById(R.id.storeID_textView);
        mStoreIDTextView.setText(storeID);

        latitude = getIntent().getStringExtra(EXTRA_LATITUDE);
        mLatitudeTextView = (TextView)findViewById(R.id.latitude_textView);
        mLatitudeTextView.setText(latitude);

        longitude = getIntent().getStringExtra(EXTRA_LONGITUDE);
        mLongitudeTextView = (TextView)findViewById(R.id.longitude_textView);
        mLongitudeTextView.setText(longitude);
    }//end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
