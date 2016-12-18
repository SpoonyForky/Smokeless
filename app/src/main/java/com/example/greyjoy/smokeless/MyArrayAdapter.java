package com.example.greyjoy.smokeless;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<StoreItem> {
    private List<StoreItem> storeList;
    private Context context;
    private int resource;

    public MyArrayAdapter (Context context, int resource, List<StoreItem> storeList){
        super(context,resource,storeList);

        this.context = context;
        this.resource = resource;
        this.storeList = storeList;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
     if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent,false);
        }

        TextView lstStoreName = (TextView) convertView.findViewById(R.id.listViewName);
        RatingBar lstRating = (RatingBar) convertView.findViewById(R.id.listViewRating);



        lstStoreName.setText(storeList.get(position).getName());
        lstRating.setIsIndicator(true);
        lstRating.setRating(storeList.get(position).getRank());

        return convertView;
    }
}
