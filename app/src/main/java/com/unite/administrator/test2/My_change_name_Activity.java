package com.unite.administrator.test2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import data.LocalOnlionUserInfo;
import data.UserDB;
import unit.BaseActivity;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/22.
 */
public class My_change_name_Activity extends BaseActivity {
    private UserDB userDB=null;
    private LocalOnlionUserInfo localOnlionUserInfo=null;
    private String username;
    private int userid;
    private EditText editText=null;
    private Button completeButton=null;
    private Button callbackButton=null;
    private GetUrl getUrl=null;
    private String url=null;
    private Boolean ischange;
    private Boolean isconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_name);

        userDB=new UserDB(this);
        localOnlionUserInfo=userDB.findOnlionUser();
        username=localOnlionUserInfo.getUsername();
        userid=localOnlionUserInfo.getId();

        editText=(EditText)findViewById(R.id.my_name_change_edittext);
        editText.setText(username);

        completeButton=(Button)findViewById(R.id.name_change_complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl = new GetUrl();
                url = getUrl.changeName(userid, editText.getText().toString());
                ischange = false;
                isconnect = false;
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        try {
                            URL url = new URL(params[0]);
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.setConnectTimeout(3000);
                            InputStream in = urlConnection.getInputStream();
                            InputStreamReader inr = new InputStreamReader(in);
                            BufferedReader bur = new BufferedReader(inr);
                            isconnect = true;
                            if (bur.readLine().equals("1")) {
                                ischange = true;
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return ischange;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean) {
                            userDB.changeName(editText.getText().toString(),userid);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(url);
            }
        });

        callbackButton=(Button)findViewById(R.id.name_change_callback_button);
        callbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
