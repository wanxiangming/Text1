package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import data.JsonDeal;
import data.LocalOnlionUserInfo;
import data.UserDB;
import data.Writer;
import unit.BaseActivity;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/7.
 */
public class Create_Team_Activity extends BaseActivity {
    private Button create_button=null;
    private Button cllback_button=null;
    private EditText create_edittext=null;
    private int creat_edittxt_length;
    private Intent intent=null;
    private int userid;

//// TODO: 2015/8/9 创建团队activity打开的时候edittext自动弹出键盘
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        intent=getIntent();
        userid=intent.getIntExtra("userid",0);

        create_edittext=(EditText)findViewById(R.id.create_activity_edittext);
        create_button=(Button)findViewById(R.id.create_activity_finish_button);
        cllback_button=(Button)findViewById(R.id.creat_acticity_callback_button);

        create_button.setOnClickListener(new create_button_onclicklistener());
        cllback_button.setOnClickListener(new callback_button_onclicklistener());
    }

    private class callback_button_onclicklistener implements OnClickListener{
        @Override
        public void onClick(View v) {
//            Intent intent=new Intent(Create_Team_Activity.this,Main_Activity.class);
//            intent.putExtra("isOpenMenu",true);
//            startActivity(intent);
            Create_Team_Activity.this.finish();
        }
    }


    private class create_button_onclicklistener implements OnClickListener {
        int iscreate=0; //判断是否注册成功
        int isgetinput=0;   //判断是否有数据从服务器返回

        @Override
        public void onClick(View v) {
            creat_edittxt_length=create_edittext.getText().toString().length();
            if(creat_edittxt_length==0){
                Toast.makeText(getApplicationContext(),"名字不能为空",Toast.LENGTH_SHORT).show();
            }else{
                new AsyncTask<String,Void,JsonDeal>(){
                    @Override
                    protected JsonDeal doInBackground(String... params) {
                        URL url= null;
                        JsonDeal jsonDeal=null;
                        try {
                            url = new URL(params[0]);
                            URLConnection urlConnection=url.openConnection();
                            urlConnection.setConnectTimeout(500);
                            InputStream inputStream=urlConnection.getInputStream();
                            isgetinput=1;
                            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
                            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                            String read;
                            read=bufferedReader.readLine();
                            Log.i("json", read);
                            if(read.equals("1")){
                                iscreate=1;
                                read=bufferedReader.readLine();
                                Intent intent=new Intent(Create_Team_Activity.this,Create_team_tip.class);
                                intent.putExtra("teaminfo",read);
                                startActivity(intent);
//                                infoDeal=new InfoDeal(Create_Team_Activity.this,userid);
//                                jsonDeal=infoDeal.updateLocalTeamInfo();

//                                try {
//                                    //Log.i("json",read);
//                                    JSONObject input=new JSONObject(read);
//                                    String teamid=input.getString("team_id");
//                                    String teamname=input.getString("team_name");
//                                    String[] strings=new String[2];
//                                    strings[0]=teamid;
//                                    strings[1]=teamname;
//                                    Intent intent=new Intent(Create_Team_Activity.this,Create_team_tip.class);
//                                    intent.putExtra("teaminfo",strings);
//                                    startActivity(intent);
//                                    //Log.i("json",a);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                Create_Team_Activity.this.finish();
                            }else{
                                iscreate=0;
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return jsonDeal;
                    }

                    @Override
                    protected void onPostExecute(JsonDeal aVoid) {
                        super.onPostExecute(aVoid);
                        UserDB userDB=new UserDB(Create_Team_Activity.this);
//                        Writer writer=new Writer(userDB,userid);
//                        writer.updateLocalTeamInfo(aVoid);
                        if(iscreate==1 && isgetinput==1){
                            Toast.makeText(getApplicationContext(),"创建成功",Toast.LENGTH_SHORT).show();
                        }else if(isgetinput==0){
                            Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(geturl());
            }

        }
    }

    private String geturl(){
        UserDB userDB=new UserDB(Create_Team_Activity.this);
        LocalOnlionUserInfo localOnlionUserInfo=userDB.findOnlionUser();
        GetUrl url=new GetUrl();
        String url1=create_edittext.getText().toString();
        //Log.i("create_edittext", url1);
        String url2=url.getcreateURL(url1,localOnlionUserInfo.getId());
        return url2;
    }
}
