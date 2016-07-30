package com.unite.administrator.test2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
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
 * Created by Administrator on 2015/8/11.
 */
public class Join_Activity extends BaseActivity {
    private EditText editText=null;
    private Button top_button=null;
    private Button complete_button=null;
    private String URl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        editText=(EditText)findViewById(R.id.join_activity_edittext);

        complete_button=(Button)findViewById(R.id.join_activity_button);
        complete_button.setOnClickListener(new complete_button_listener());

        top_button=(Button)findViewById(R.id.join_activity_top_button);
        top_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class complete_button_listener implements OnClickListener{
        private int OK=0;
        private int userid;
        private int team_id;
        private Boolean isjoin;

        @Override
        public void onClick(View v) {
            String string=editText.getText().toString();
            if(string.length()!=0){
                team_id=Integer.valueOf(string);
                UserDB userDB=new UserDB(Join_Activity.this);
                LocalOnlionUserInfo userInfo=userDB.findOnlionUser();
                //// TODO: 2015/8/25 这个是否加入了该团队的判断，放到服务器上去判断
                userid=userInfo.getId();
                //isjoin=userDB.isMemberOfThis(team_id, userid);
                //userDB.closeAll();
                if(isjoin){
                    Toast.makeText(Join_Activity.this,"您已加入该团队",Toast.LENGTH_SHORT).show();
                }else{
                    GetUrl url=new GetUrl();
                    URl=url.request_for_join(userid,team_id);
                    new AsyncTask<String,Void,Void>(){
                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                URL url1=new URL(params[0]);
                                URLConnection urlConnection=url1.openConnection();
                                urlConnection.setConnectTimeout(3000);
                                InputStream inputStream=urlConnection.getInputStream();
                                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf_8");
                                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                                if(bufferedReader.readLine().equals("1")){
                                    OK=1;
                                }else{
                                    OK=0;
                                }
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
                            if(OK==1){
                                Join_Activity.this.finish();
                                Toast.makeText(getApplicationContext(),"请求发送成功",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"请输入正确的团队ID",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(URl);
                }
            }else{
                Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
