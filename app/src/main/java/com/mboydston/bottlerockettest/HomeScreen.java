package com.mboydston.bottlerockettest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

/**
 * Main Activity for the application. Launches async task to download and store json info, and then
 * populates list view with information. User can click an item to launch ListItem.java
 */

public class HomeScreen extends AppCompatActivity {

    private Button mFetchButton;
    private ListView mDisplayDataListView;
    private static String file_url =
            "http://sandbox.bottlerocketapps.com/BR_Android_CodingExam_2015_Server/stores.json";

    //JSON Names
    private static final String TAG_STORES = "stores";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_CITY = "city";
    private static final String TAG_NAME = "name";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_STORELOGOURL = "storeLogoURL";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_STOREID = "storeID";
    private static final String TAG_STATE = "state";

    private JSONArray stores = null;
    private JSONObject[] storesArray = null;
    private static JSONNode[] jsons = null;
    private static String[] phoneNumbers = null;
    private static String[] storeAddresses = null;
    private static String[] imageUrls = null;
    private static Bitmap[] logos = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Listeners
        mFetchButton = (Button) findViewById(R.id.fetch_button);
        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check to see if connection is availabe before attempting to download data
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                //If wifo connection available, execute fetch data. If not, notify user
                if (mWifi.isConnected() || mNetwork.isConnected()) {
                    new FetchJSON().execute(file_url);
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Unavailable",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //set up listener for list item clicks
        mDisplayDataListView = (ListView) findViewById(R.id.displayData_listView);
        mDisplayDataListView.setClickable(true);
        mDisplayDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String values
                Intent intent = new Intent(HomeScreen.this, ListItem.class);
                intent.putExtra(ListItem.EXTRA_NAME, jsons[position].getName());
                intent.putExtra(ListItem.EXTRA_ADDRESS, jsons[position].getAddress());
                intent.putExtra(ListItem.EXTRA_CITY, jsons[position].getCity());
                intent.putExtra(ListItem.EXTRA_LATITUDE, jsons[position].getLatitude());
                intent.putExtra(ListItem.EXTRA_LONGITUDE, jsons[position].getLongitude());
                intent.putExtra(ListItem.EXTRA_PHONE, jsons[position].getPhone());
                intent.putExtra(ListItem.EXTRA_STATE, jsons[position].getState());
                intent.putExtra(ListItem.EXTRA_STOREID, jsons[position].getStoreID());
                intent.putExtra(ListItem.EXTRA_ZIP, jsons[position].getZip());


                //Bitmap
                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    jsons[position].getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);

                    stream.close();
                    intent.putExtra(ListItem.EXTRA_LOGO, filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }//end onCreate

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }//end onResume

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private class FetchJSON extends AsyncTask<String, String, JSONNode[]> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(HomeScreen.this);
            pDialog.setMessage("Fetching Store Info ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONNode[] doInBackground(String... url) {
            JSONParser jParser = new JSONParser();
            //Get JSONObject from url
            JSONObject json = jParser.getJSONObj(url[0]);
            try {
                //get JSON Array
                stores = json.getJSONArray(TAG_STORES);
                //create array of JSONNodes
                jsons = new JSONNode[stores.length()];

                storesArray = new JSONObject[stores.length()];

                for (int i = 0; i < stores.length(); i++) {
                    storesArray[i] = stores.getJSONObject(i);

                    jsons[i] = new JSONNode(
                            storesArray[i].getString(TAG_ADDRESS),
                            storesArray[i].getString(TAG_CITY),
                            storesArray[i].getString(TAG_NAME),
                            storesArray[i].getString(TAG_LATITUDE),
                            storesArray[i].getString(TAG_ZIPCODE),
                            storesArray[i].getString(TAG_STORELOGOURL),
                            storesArray[i].getString(TAG_PHONE),
                            storesArray[i].getString(TAG_LONGITUDE),
                            storesArray[i].getString(TAG_STOREID),
                            storesArray[i].getString(TAG_STATE));
                }//end for

                //notify user we
                publishProgress();

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                for (int i = 0; i < stores.length(); i++) {
                    jsons[i].setBitmap(jsons[i].getLogoURL(), width);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return json;
            return jsons;
        }//end doInBackground

        @Override
        protected void onProgressUpdate(String... progress) {
            pDialog.setMessage("Fetching Store Info ... Done\nFetching Store Logos ... ");
        }

        @Override
        protected void onPostExecute(JSONNode[] jsons) {
            pDialog.dismiss();

            phoneNumbers = new String[jsons.length];
            storeAddresses = new String[jsons.length];
            imageUrls = new String[jsons.length];
            logos = new Bitmap[jsons.length];

            for (int i = 0; i < jsons.length; i++) {
                phoneNumbers[i] = jsons[i].getPhone();
                storeAddresses[i] = jsons[i].getAddress();
                imageUrls[i] = jsons[i].getLogoURL();
                logos[i] = jsons[i].getBitmap();
            }

            populateList();
        }//end onPostExecute
    }//end FetchJSON

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void populateList() {
        if (phoneNumbers != null && storeAddresses != null && logos != null) {
            mFetchButton.setVisibility(View.INVISIBLE);
            MyListAdapter adapter = new MyListAdapter(HomeScreen.this, phoneNumbers, storeAddresses, logos);
            mDisplayDataListView.setAdapter(adapter);
        }
    }//end populateList

    ///////////////////////////////////////////////////////////////////////////////////////////////////
}//end class HomeScreen



