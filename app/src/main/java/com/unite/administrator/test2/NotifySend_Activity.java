package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

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
import java.util.List;

import data.LocalOnlionUserInfo;
import data.Request;
import data.UserDB;
import listObject.SerializableForTrans_teamInfo;
import unit.BaseActivity;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/16.
 */
public class NotifySend_Activity extends BaseActivity {
    private List<SerializableForTrans_teamInfo> list;
    private EditText edittext=null;
    private Button button=null;
    private int userid;
    private Boolean issend;
    private Gson gson=null;
    private Request request=null;

    private final String sendsucceed="notify.send.succeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_edit);

        Intent intent=getIntent();
        list= (List<SerializableForTrans_teamInfo>) intent.getSerializableExtra("list");
//        for (int i=0;i<list.size();i++){
//            Log.i("notifysend",list.get(i).getTeam_name());
//        }

        UserDB u=new UserDB(this);
        LocalOnlionUserInfo l=u.findOnlionUser();
        userid=l.getId();

        edittext=(EditText)findViewById(R.id.notify_edittext);
        edittext.setFocusable(true);
        edittext.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        button=(Button)findViewById(R.id.notify_send_button);
        button.setOnClickListener(new buttonlistener());


    }


    private class buttonlistener implements OnClickListener {
        String string;

        @Override
        public void onClick(View v) {
            issend=false;
            string=edittext.getText().toString();
            if(string.length()!=0){
                gson=new Gson();
                new AsyncTask<Integer,Void,Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        GetUrl geturl=new GetUrl();
                        request=new Request();
                        request.setFlag("sendnotify");
                        request.setMessages(geturl.encoder(string));
                        request.setUserid(userid);
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getIschecke()){
                                request.setTeam_id(list.get(i).getTeam_id());
                                String json=gson.toJson(request);
                                Log.i("json", json);
                                String u=geturl.sendNotify();
                                try {
                                    URL url1 = new URL(u);
                                    HttpURLConnection connection= (HttpURLConnection) url1.openConnection();

                                    connection.setConnectTimeout(5000);
                                    connection.setReadTimeout(5000);
                                    connection.setDoOutput(true);
                                    connection.setRequestMethod("POST");
                                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                    connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
                                    OutputStreamWriter osw=new OutputStreamWriter(connection.getOutputStream(),"utf-8");
                                    BufferedWriter bw=new BufferedWriter(osw);
                                    bw.write(json);
                                    bw.flush();


                                    InputStream in=connection.getInputStream();
                                    InputStreamReader inr=new InputStreamReader(in);
                                    BufferedReader bur=new BufferedReader(inr);
                                    issend=true;
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if(issend){
                            Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(sendsucceed);
                            sendBroadcast(intent);
                            finish();
                        }else{
                            Toast toast=Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,0);
                            toast.show();
                        }
                    }
                }.execute(userid);

            }else {
                Toast.makeText(getApplicationContext(),"内容不能为空",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
