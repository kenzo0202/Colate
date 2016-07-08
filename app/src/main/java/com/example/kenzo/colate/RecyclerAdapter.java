package com.example.kenzo.colate;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<CustomDataObject> my_List;
    public RecyclerAdapter(ArrayList<CustomDataObject> my_List, Context context){
        this.context = context;
        this.my_List = my_List;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        CustomDataObject item = my_List.get(position);
        viewHolder.textView.setText(item.getTitle());
        Log.d("number",item.getImageType()+"");
        if(item.getImageType() == 1){
            Log.d("number","いけてるよ！！");
            viewHolder.icon.setImageResource(R.drawable.kenzo);
        }else if(item.getImageType() == 2){
            viewHolder.icon.setImageResource(R.drawable.shinoki);
        }

    }

    @Override
    public int getItemCount() {
        return this.my_List.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView icon;

        public ViewHolder(View v, Context context) {
            super(v);
            textView = (TextView)v.findViewById(R.id.category_name);
            icon =(ImageView)v.findViewById(R.id.category_icon);

        }
    }
}
