package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import data.JsonDeal;
import data.LocalOnlionUserInfo;
import data.Request;
import unit.BaseActivity;
import unit.GetDeviceId;
import unit.GetUrl;
import data.UserDB;

import static data.ToMd5.stringToMD5;


public class Login_Activity extends BaseActivity {
    private EditText login_ET_acon=null;
    private EditText login_ET_pass=null;
    private Button login_BT_login=null;
    private Button login_BT_register=null;
    private String url;
    private UserDB userDB=null;
    private String isonlion;
    private GetDeviceId getDeviceId=null;
    private String deviceId=null;

    private String acon;    //这几个数都是在URL()函数中被赋值的
    private String pass;
    private int id;
    private String username;

    int isChangedActivity=0;    //判断页面是否跳转


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_ET_acon=(EditText)findViewById(R.id.login_ET_acon);
        login_ET_pass=(EditText)findViewById(R.id.login_ET_pass);
        login_BT_register=(Button)findViewById(R.id.lonin_BT_register);
        login_BT_login=(Button)findViewById(R.id.lonin_BT_login);
        login_BT_register.setOnClickListener(new login_BT_rigester_onclick());
        login_BT_login.setOnClickListener(new login_BT_login_onclick());

        getDeviceId=new GetDeviceId(this);
        deviceId=getDeviceId.getId();
    }


/*
登陆按钮的监听器
 */
    private  class login_BT_login_onclick implements OnClickListener{
        private int islogin=0;
        private int isconnect=0;
        private int id;
        private int internetIll=0;
        @Override
        public void onClick(View v) {
            userDB=new UserDB(Login_Activity.this);    //把DB初始化一下，这样后面的函数可以直接调用
            internetIll=0;
            GetUrl getUrl=new GetUrl();
            String url=getUrl.getloninURL();
            new AsyncTask<String,Void,Void>(){
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if(internetIll==0){
                        Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_SHORT).show();
                    }else if(islogin==1 && isconnect==1){
                        if(isChangedActivity==0){
                            if(userDB.findUser(acon)){ //判断一下，如果本地没有用户数据则添加数据，如果已经有数据则把本地数据更新一下
                                //Log.i("login",acon);
                                userDB.updatauserinfo(acon,pass,username);
                            }else {
                                //Log.i("login",acon);
                                userDB.adduser(acon, pass, id,username);
                            }
                            userDB.onlionstat(id, 1);   //设置用户在线状态
                            Intent intent=new Intent(Login_Activity.this,Main_Activity.class);
                            intent.putExtra("login",true);
                            startActivity(intent);
                            Login_Activity.this.finish();
                            //userDB.closeAll();
                            isChangedActivity=1;
                        }
                    }else if(isconnect==1){
                        Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected Void doInBackground(String... params) {
                    try {
                        URL url1 = new URL(params[0]);
                        HttpURLConnection connection= (HttpURLConnection) url1.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("Content-Length", String.valueOf(getedittext().length()));
                        OutputStreamWriter osw=new OutputStreamWriter(connection.getOutputStream(),"utf-8");
                        BufferedWriter bw=new BufferedWriter(osw);
                        bw.write(getedittext());
                        bw.flush();

                        InputStream In=connection.getInputStream();
                        InputStreamReader Inr=new InputStreamReader(In,"utf-8");
                        BufferedReader Br=new BufferedReader(Inr);
                        String reader=Br.readLine();
                        Gson gson=new Gson();
                        JsonDeal jsonDeal=gson.fromJson(reader,JsonDeal.class);
                        isconnect=1;
                        //Log.i("json",String.valueOf(jsonDeal.getIsLogin()));
                        if(jsonDeal.getIsLogin()==0){
                            islogin=0;
                            internetIll=1;
                        }else if(jsonDeal.getIsLogin()==1){
                            id= jsonDeal.getId();
                            username=jsonDeal.getUsername();
                            islogin=1;
                            internetIll=1;
                        }
                        In.close();
                        Inr.close();
                        Br.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(url);
        }
    }

    private class login_BT_rigester_onclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Login_Activity.this,Register_Activity.class);
            startActivity(intent);
        }
    }


    private String getedittext(){
        Request request= new Request();
        acon=login_ET_acon.getText().toString();
        pass=stringToMD5(login_ET_pass.getText().toString());
        request.setAccon(login_ET_acon.getText().toString());
        request.setPassword(stringToMD5(login_ET_pass.getText().toString()));
        request.setDeviceId(deviceId);
        request.setFlag("login");
        Gson gson=new Gson();
        String json=gson.toJson(request);
        //Log.i("json",json);
        return json;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isChangedActivity=0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}