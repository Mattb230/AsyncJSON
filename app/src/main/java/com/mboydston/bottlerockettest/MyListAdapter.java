package com.mboydston.bottlerockettest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Adapter to display store Image, Address, and Phone number.
 * Uses display_data_layout
 */
public class MyListAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mValues;
    private final String[] mValues2;
    private final Bitmap[] mValues3;

    public MyListAdapter(Context context, String[] values, String[] values2, Bitmap[] values3){
        super(context, -1, values);
        mContext = context;
        mValues = values;
        mValues2 = values2;
        mValues3 = values3;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.display_data_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image1);

        textView.setText(mValues[position]);
        textView2.setText(mValues2[position]);
        imageView.setImageBitmap(mValues3[position]);


        return rowView;
    }
}


