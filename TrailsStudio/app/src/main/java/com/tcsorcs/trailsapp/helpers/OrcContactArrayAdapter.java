package com.tcsorcs.trailsapp.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.OrcContact;
import com.tcsorcs.trailsapp.managers.DisplayManager;

import java.util.ArrayList;

public class OrcContactArrayAdapter<OrcContact> extends ArrayAdapter<OrcContact> {
    private final Context context;
    private final ArrayList<OrcContact> values;

    public OrcContactArrayAdapter(Context context, ArrayList<OrcContact> values) {
        super(context, R.layout.listview_orc_contact, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_orc_contact, parent, false);

        com.tcsorcs.trailsapp.helpers.OrcContact contact=((com.tcsorcs.trailsapp.helpers.OrcContact)values.get(position));


        TextView textView = (TextView) rowView.findViewById(R.id.orc_contact_name);
        String contactName=contact.getContactName();
        textView.setText(contactName);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.orc_contact_icon);


        int resId  = DisplayManager.getInstance().main_activity.getResources().getIdentifier("ic_launcher", "drawable", DisplayManager.getInstance().main_activity.getPackageName());


        imageView.setImageResource(resId);

        if(contact.isSelected()){
            rowView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            rowView.setBackgroundColor(Color.TRANSPARENT);
        }



        return rowView;
    }
}