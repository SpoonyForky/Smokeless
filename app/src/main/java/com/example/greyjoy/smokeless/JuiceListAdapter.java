package com.example.greyjoy.smokeless;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by GreyJoy on 12/29/2016.
 */

public class JuiceListAdapter extends ArrayAdapter<JuiceItem> {

    private List<JuiceItem> juiceList;
    private Context context;
    private int resource;

    public JuiceListAdapter (Context context, int resource, List<JuiceItem> juiceList){
        super(context,resource,juiceList);
        this.context = context;
        this.resource = resource;
        this.juiceList = juiceList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }
        ImageButton juiceImage = (ImageButton) convertView.findViewById(R.id.juiceListImage);


        return convertView;
    }
}
