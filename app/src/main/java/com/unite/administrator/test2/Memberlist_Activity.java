package com.unite.administrator.test2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import adapter.MemberListAdapter;
import data.JsonDeal;
import data.UserDB;
import data.Writer;
import listObject.Member;
import temp.TempMember;
import unit.BaseActivity;
import unit.GetUrl;

import android.view.View.OnClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Memberlist_Activity extends BaseActivity {
    private Button leaveButton = null;
    private int userid;
    private int team_id;
    private UserDB userDB = null;
    private Boolean isLeaving;
    private Boolean isKing;
    private ListView listView = null;
    private MemberListAdapter memberListAdapter = null;
    private TempMember tempMember = null;
    private Button callbackButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberlist);

        userDB = new UserDB(Memberlist_Activity.this);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);
        team_id = intent.getIntExtra("team_id", 0);
        isKing = intent.getBooleanExtra("isKing", false);
        //Toast.makeText(getApplicationContext(), String.valueOf(intent.getIntExtra("team_id", 0)), Toast.LENGTH_SHORT).show();

        callbackButton = (Button) findViewById(R.id.memberlist_acticity_callback_button);
        callbackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leaveButton = (Button) findViewById(R.id.leave_team_button);
        if (isKing) {
            leaveButton.setText("解散团队");
        } else {
            leaveButton.setText("退出团队");
        }
        leaveButton.setOnClickListener(new LeaveTeamButtonListener());

        tempMember = new TempMember();
        tempMember.getMemberInfo(team_id, new TempMember.OnCallBackListener() {
            @Override
            public void OnConnect(List<Member> list) {
                listView = (ListView) findViewById(R.id.mamber_listview);
                memberListAdapter = new MemberListAdapter(Memberlist_Activity.this, R.layout.style_memberlist, list);
                listView.setAdapter(memberListAdapter);
            }

            @Override
            public void OnNoConnect() {
                Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class LeaveTeamButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Memberlist_Activity.this);
            dialogBuilder.setTitle("提示");
            dialogBuilder.setMessage("你确定要这么做吗");
            dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {    //当用户点击确定按钮的时候，执行删除操作
                    isLeaving = false;
                    GetUrl getUrl = new GetUrl();
                    String url = getUrl.removeTeam(userid, team_id);
                    new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... params) {
                            try {
                                URL Url = new URL(params[0]);
                                URLConnection urlConnection = Url.openConnection();
                                urlConnection.setConnectTimeout(5000);
                                InputStream in = urlConnection.getInputStream();
                                InputStreamReader inr = new InputStreamReader(in);
                                BufferedReader bur = new BufferedReader(inr);
                                if (bur.readLine().equals("1")) {     //当服务器的返回值是1时，才执行本地的删除操作
                                    isLeaving = true;
                                } else {

                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return isLeaving;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if (aBoolean) {
                                userDB.clearTeamInfo(userid, team_id);
                                Memberlist_Activity.this.finish();
                            }
                        }
                    }.execute(url);

                }
            });
            dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            Dialog dialog = dialogBuilder.create();
            dialog.show();
        }
    }
}

