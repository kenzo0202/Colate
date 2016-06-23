package com.example.kenzo.colate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItemDetailActivity extends Activity{
    private TextView mTitle;
    private TextView mDetail;
    private TextView mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        Intent intent = getIntent();

        mTitle = (TextView)findViewById(R.id.item_detail_title);
        String title = intent.getStringExtra("title");
        mTitle.setText(title);
        mDetail = (TextView)findViewById(R.id.item_detail_detail);
        String detail = intent.getStringExtra("detail");
        mDetail.setText(detail);
        mUrl = (TextView)findViewById(R.id.item_detail_url);
        String url = intent.getStringExtra("url");
        mUrl.setText(url);
    }
}
