package com.example.kenzo.colate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private RequestQueue mQueue;
    private TwitterLoginButton twiiterloginButton;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "SrHHJonYbB67yc6IbW13RDO8y";
    private static final String TWITTER_SECRET = "ldeGIwT0bGEuTaCev7v9B7hmulERMfCK5Fk2zrWKrhkbuk2GMz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Twitterログイン
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        twiiterloginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        twiiterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                String msg = "@"+ session.getUserName() + "Logged in (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),"ログインに失敗しました",Toast.LENGTH_SHORT).show();
            }
        });

        //Facebookログイン
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);


        List<String> permissionNeeds = Arrays.asList("public_profile");
        loginButton.setReadPermissions(permissionNeeds);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //ログインが成功したとき
                //LoginResultパラメータに新しいAccessTokenと、最後に付与または拒否されたアクセス許可が追加される
                //To do Graph APIにリクエストを送る
                GraphRequest graphrequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //リクエストが成功した時JSONObjectにデータが入ってる
                        //自動的にレスポンスデータがJSONObjectにデシリアライズされる
                        registerMember(object);

                    }
                });
                Bundle parameters = new Bundle();
                //fieldsに欲しい情報を追加する
                parameters.putString("fields","id,name,gender");
                graphrequest.setParameters(parameters);
                graphrequest.executeAsync();
            }

            @Override
            public void onCancel() {
                //ログインがキャンセルされた時

            }

            @Override
            public void onError(FacebookException error) {
                //ログインがエラーした時
            }

            });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

                //ログアウト処理をした場合
                if (currentAccessToken == null) {
                    loggedout();
                }
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
    }

    /**
     * volleyを使用して、データベースにGraphAPIから取ってきた情報を突っ込む
     * @param object
     */
    private void registerMember(final JSONObject object) {
        mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject responseJsonObject = new JSONObject(response);
                        int status = responseJsonObject.getInt("status");
                        if (status == 1) {
                            Toast toast = Toast.makeText(getApplicationContext(), "ログイン成功しました", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "ログイン失敗しました", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("error","エラーです。");
                }
                //成功した時

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //エラー出した時
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
            }
        }){
            /**
             * パラメータを設定
             * @return params
             */
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String,String>();


            try {
                Log.d("param",object.getString("id"));
                params.put("sns_user_id",object.getString("id"));
                params.put("name",object.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return params;
        }};

        //リクエスト送信
        mQueue.add(stringRequest);
    }

    /**
     * ログアウトした時の処理
     * @params void
     */
    private void loggedout() {
        Toast toast = Toast.makeText(getApplicationContext(),"ログアウトしました",Toast.LENGTH_LONG);
        toast.show();
    }

    //ログインが完了した後の、結果を受け取る
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twiiterloginButton.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}

