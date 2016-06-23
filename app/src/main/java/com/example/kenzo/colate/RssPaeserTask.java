package com.example.kenzo.colate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class RssPaeserTask extends AsyncTask<String,Integer,RssListAdapter> {
    private MainActivity mActivity;
    private RssListAdapter mAdapter;
    private ProgressDialog mDialog;

    //コンストラクタ
    public RssPaeserTask(MainActivity activity, RssListAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage("少しお待ち下さい");
        mDialog.setMax(100);
        mDialog.show();
    }

    @Override
    protected RssListAdapter doInBackground(String... params) {
        RssListAdapter result = null;
        try{
            //HTTP経由でアクセスして、InputStreamを取得する
            URL url = new URL(params[0]);
            InputStream inputStream = url.openConnection().getInputStream();
            result = parseXml(inputStream);
        }catch (Exception e){
            e.getStackTrace();
        }

        return result;

    }

    private RssListAdapter parseXml(InputStream inputStream)throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();

        try{
            parser.setInput(inputStream,"UTF-8");
            int eventType = parser.getEventType();
            Item currentItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                String tag;
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if(tag.equals("item")){
                            currentItem = new Item();
                        }else if(currentItem !=null){
                            if(tag.equals("title")) {
                                currentItem.setTitle(parser.nextText());
                            }else if(tag.equals("link")) {
                                currentItem.setUrl(parser.nextText());
                            }else if (tag.equals("description")) {
                                currentItem.setDescription(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            mAdapter.add(currentItem);
                        }
                        break;
                }
                eventType = parser.next();
            }
        }catch (Exception e){
            Log.d("error",e.getMessage());
        }

        return mAdapter;
    }

    @Override
    protected void onPostExecute(RssListAdapter result) {
        mDialog.dismiss();
        mActivity.setListAdapter(result);
    }
}
