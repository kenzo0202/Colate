package com.example.kenzo.colate;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;

public class MainActivity extends ListActivity  implements
        SwipeRefreshLayout.OnRefreshListener{
    private final static String RSS_URL = "http://searchranking.yahoo.co.jp/rss/burst_ranking-rss.xml";
    private ArrayList<Item> mitems;
    private RssListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputData();


//        //firebaseの初期化
//        Firebase.setAndroidContext(this);
//        Firebase myFirebaseRef = new Firebase("https://colate-485e4.firebaseio.com/");
//        //データベースへの書き込み
//        myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");


        //android-bootstrapの導入
        TypefaceProvider.registerDefaultIconSets();

        mitems = new ArrayList<Item>();
        mAdapter = new RssListAdapter(this,mitems);

        createSwipeRefreshlayout();

        RssPaeserTask task = new RssPaeserTask(this,mAdapter);
        task.execute(RSS_URL);
        initTabs();
    }

    private void inputData() {
        ArrayList<CustomDataObject> customDataObjectArraylist = new ArrayList<>();
        //リサイクルビューの記述
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        //動的にviewが変化しない場合、下記のように記述すると効率が良い
        recyclerView.setHasFixedSize(true);

        RecyclerAdapter recycleadaptaer = new RecyclerAdapter(customDataObjectArraylist,this);

        CustomDataObject customdataobject = new CustomDataObject();
        customdataobject.setTitle("おもしろ");
        customdataobject.setImageType(1);
        customDataObjectArraylist.add(customdataobject);

        CustomDataObject customdataobject1 = new CustomDataObject();
        customdataobject1.setTitle("かなしい");
        customdataobject1.setImageType(2);
        customDataObjectArraylist.add(customdataobject1);

        recyclerView.setAdapter(recycleadaptaer);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
    }

    private void createSwipeRefreshlayout() {

        mRefreshlayout = (SwipeRefreshLayout)findViewById(R.id.swipelayout);
        mRefreshlayout.setOnRefreshListener(this);

    }

    protected void initTabs(){
        try{
            TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
            tabHost.setup();
            TabHost.TabSpec spec;

            spec = tabHost.newTabSpec("Tab1")
                    .setIndicator("アカウント")
                    .setContent(R.id.linear1);
            tabHost.addTab(spec);
            spec = tabHost.newTabSpec("Tab2")
                    .setIndicator("投稿一覧")
                    .setContent(R.id.linear2);
            tabHost.addTab(spec);
            spec = tabHost.newTabSpec("Tab3")
                    .setIndicator("カテゴリー")
                    .setContent(R.id.linear3);
            tabHost.addTab(spec);

            tabHost.setCurrentTab(0);


        }catch (IllegalArgumentException e) {
        e.printStackTrace();
        }catch (RuntimeException e) {
        e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item item = mitems.get(position);
        Intent intent = new Intent(this,ItemDetailActivity.class);
        intent.putExtra("title",item.getTitle().toString());
        intent.putExtra("detail",item.getDescription().toString());
        intent.putExtra("url",item.getUrl().toString());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mitems = new ArrayList();
        mAdapter = new RssListAdapter(this, mitems);
        // タスクはその都度生成する
        RssPaeserTask task = new RssPaeserTask(this, mAdapter);
        task.execute(RSS_URL);
        mRefreshlayout.setRefreshing(false);
    }
}
