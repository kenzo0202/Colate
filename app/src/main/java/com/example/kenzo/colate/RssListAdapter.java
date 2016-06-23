package com.example.kenzo.colate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RssListAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflator;
    private TextView mTitle;
    private TextView mdescription;


    public RssListAdapter(Context context, List<Item> objects){
        super(context,0, objects);
        mInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(convertView == null){
            view = mInflator.inflate(R.layout.item_row,null);
        }


        //現在参照しているリストの位置からアイテムを取得する
        Item item = this.getItem(position);
        if(item != null){
            String title = item.getTitle().toString();
            mTitle = (TextView)view.findViewById(R.id.item_title);
            mTitle.setText(title);
            String detail = item.getDescription().toString();
            mdescription = (TextView)view.findViewById(R.id.item_detail);
            mdescription.setText(detail);
        }

        return view;
    }
}
