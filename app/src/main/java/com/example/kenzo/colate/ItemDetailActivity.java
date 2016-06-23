package com.example.kenzo.colate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends Activity {

    @BindView(R.id.item_detail_title)
    TextView itemDetailTitle;
    @BindView(R.id.item_detail_detail)
    TextView itemDetailDetail;
    @BindView(R.id.item_detail_url)
    TextView itemDetailUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        itemDetailTitle = (TextView) findViewById(R.id.item_detail_title);
        String title = intent.getStringExtra("title");
        itemDetailTitle.setText(title);
        itemDetailDetail = (TextView) findViewById(R.id.item_detail_detail);
        String detail = intent.getStringExtra("detail");
        itemDetailDetail.setText(detail);
        itemDetailUrl = (TextView) findViewById(R.id.item_detail_url);
        String url = intent.getStringExtra("url");
        itemDetailUrl.setText(url);
    }
}
