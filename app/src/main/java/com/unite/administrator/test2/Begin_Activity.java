package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import data.LocalOnlionUserInfo;
import unit.GetDeviceId;
import unit.GetUrl;
import data.*;

/**
 * Created by Administrator on 2015/8/8.
 */
public class Begin_Activity extends Activity {
    private String url;
    private Timer timer = null;
    private GetDeviceId getDeviceId = null;
    private String deviceId = null;
    private String json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        getDeviceId = new GetDeviceId(this);
        deviceId = getDeviceId.getId();

        timer = new Timer();
        GetUrl urlmap = new GetUrl();
        UserDB userDB = new UserDB(this);
        LocalOnlionUserInfo localOnlionUserInfo = userDB.findOnlionUser();

        //如果本地存在状态为登陆的用户，则获取它的密码和账号，与服务器取得连接,不存在则直接跳转登陆页面
        if (localOnlionUserInfo.isexsit()) {
            String accon = localOnlionUserInfo.getAccon();
            String password = localOnlionUserInfo.getPassword();
            Request request = new Request();
            Gson gson = new Gson();
            request.setAccon(accon);
            request.setPassword(password);
            request.setDeviceId(deviceId);
            request.setFlag("login");
            json = gson.toJson(request);
            url = urlmap.getloninURL();
            new AsyncTask<String, Void, Void>() {
                int isconnect = 0;
                String isonlion;

                @Override
                protected Void doInBackground(String... params) {
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
                        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(json);
                        bw.flush();

                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader Inr = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader Br = new BufferedReader(Inr);
                        String red= Br.readLine();   //服务器端，根据传过去的用户名和密码，判断是否正确，正确传回来1，否则传回来0
                        Gson gson1=new Gson();
                        JsonDeal jsonDeal=gson1.fromJson(red,JsonDeal.class);
                        isonlion=String.valueOf(jsonDeal.getIsLogin());
                        //Log.i("json", json);
                        isconnect = 1;
                        Br.close();
                        Inr.close();
                        inputStream.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (isconnect == 0) {
                        Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_LONG).show();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Intent in = new Intent(Begin_Activity.this, Main_Activity.class);
                                startActivity(in);
                                Begin_Activity.this.finish();
                            }
                        };
                        timer.schedule(task, 800);
                    } else if (isconnect == 1 && isonlion.equals("1")) {
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Intent in = new Intent(Begin_Activity.this, Main_Activity.class);
                                startActivity(in);
                                Begin_Activity.this.finish();
                            }
                        };
                        timer.schedule(task, 800);
                    } else {
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Intent in = new Intent(Begin_Activity.this, Login_Activity.class);
                                startActivity(in);
                                Begin_Activity.this.finish();
                            }
                        };
                        timer.schedule(task, 800);
                    }
                }
            }.execute(url);
        } else {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Begin_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    Begin_Activity.this.finish();
                }
            };
            timer.schedule(task, 800);
        }
    }
}
