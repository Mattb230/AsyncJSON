package com.mboydston.bottlerockettest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mboydston on 7/12/2015.
 *
 */
public class JSONParser {

    static JSONObject mJObj = null;
    static String mJString = "";

    public JSONParser(){

    }

    /**
     * Returns a JSONObject from a url
     * @param url
     * @return
     */
    public JSONObject getJSONObj(String url){

        try {
            URL JSON_url = new URL(url);
            URLConnection connection = JSON_url.openConnection();
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(JSON_url.openStream()));
            StringBuilder JSONsb = new StringBuilder();
            String line;
            while((line = in.readLine()) != null){
                JSONsb.append(line + "\n");
            }
            in.close();
            mJString = JSONsb.toString();

        } catch (MalformedURLException e) {
            Log.e("Malformed URL: ", e.getMessage());
        } catch (IOException e){
            Log.e("I/O Error: ", e.getMessage());
        }

        try{
            mJObj = new JSONObject(mJString);
        } catch (JSONException e){
            Log.e("Buffer Error" , "Error Converting result " + e.toString());
        }

        return mJObj;
    }
}
