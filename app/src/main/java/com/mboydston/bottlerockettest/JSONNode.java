package com.mboydston.bottlerockettest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mboydston on 7/13/2015.
 */
public class JSONNode {
    private String address;
    private String city;
    private String name;
    private String latitude;
    private String zip;
    private String logoURL;
    private String phone;
    private String longitude;
    private String storeID;
    private String state;
    private Bitmap bitmap;


    public JSONNode(){

    }
    public JSONNode(String address, String city, String name, String latitude, String zip,
                    String logoURL, String phone, String longitude, String storeID, String state){
        this.address = address;
        this.city = city;
        this.name = name;
        this.latitude = latitude;
        this.zip = zip;
        this.logoURL = logoURL;
        this.phone = phone;
        this.longitude = longitude;
        this.storeID = storeID;
        this.state = state;
    }
///////////////////////////////////////////////////////////

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
///////////////////////////////////////////////////////////

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
///////////////////////////////////////////////////////////

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLatitude() {
        return latitude;
    }
///////////////////////////////////////////////////////////

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
    public String getLogoURL() {
        return logoURL;
    }
///////////////////////////////////////////////////////////

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLongitude() {
        return longitude;
    }
///////////////////////////////////////////////////////////

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
///////////////////////////////////////////////////////////

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
///////////////////////////////////////////////////////////

    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
///////////////////////////////////////////////////////////

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }
    public String getStoreID() {
        return storeID;
    }
///////////////////////////////////////////////////////////
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getZip() {
        return zip;
    }
///////////////////////////////////////////////////////////
    public void setBitmap(String url, int x) {
        bitmap = getBitmapFromURL(url, x);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    //get bitmap from url and resize it to fit screen
    public Bitmap getBitmapFromURL(String src, int x) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(input);

            //code to acale bitmap to 1/5 phone screen width
            int width = bm.getWidth();
            int height = bm.getHeight();
            int ratio = width/height;
            float scaleWidth = ((float) x/5 ) / width;
            float scaleHeight = ((float) (x/5)/ratio ) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);

            return resizedBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
