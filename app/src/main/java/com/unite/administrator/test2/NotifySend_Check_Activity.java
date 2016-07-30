package com.unite.administrator.test2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.UserDB;
import listObject.SerializableForTrans_teamInfo;
import listObject.Team;
import adapter.CheckTeamListAdapter;
import temp.TempTeamInfo;
import unit.BaseActivity;

/**
 * Created by Administrator on 2015/8/16.
 */
public class NotifySend_Check_Activity extends BaseActivity {
    private int userid;
    private List<Team> list = null;
    private ListView listView = null;
    private CheckTeamListAdapter checkTeamListAdapter = null;
    private Button completeButton = null;
    private final String sendsucceed = "notify.send.succeed";
    private TextView textView = null;
    private TempTeamInfo tempTeamInfo = null;
    private Button callbackButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_checkteam);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);

        callbackButton = (Button) findViewById(R.id.sendcheck_top_button2);
        callbackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tempTeamInfo = new TempTeamInfo(this, userid);
        tempTeamInfo.getInfo(new TempTeamInfo.OnTeamInfoCallBackListener() {
            @Override
            public void onConnect(List<Team> teamList) {
                textView = (TextView) findViewById(R.id.sendcheck_textview);
                listView = (ListView) findViewById(R.id.sendcheck_teamlist_listview);
                checkTeamListAdapter = new CheckTeamListAdapter(NotifySend_Check_Activity.this, R.layout.style_sendchecklist, teamList);
                listView.setAdapter(checkTeamListAdapter);

                if (teamList.size() == 0) {
                    listView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

                completeButton = (Button) findViewById(R.id.sendcheck_top_button1);
                completeButton.setOnClickListener(new completeButtonListener(checkTeamListAdapter.getList()));
            }

            @Override
            public void onNoConnect() {
                Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });


        IntentFilter intentFilter = new IntentFilter();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        intentFilter.addAction(sendsucceed);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private class completeButtonListener implements OnClickListener {
        private List<Team> list;
        private Boolean doyoucheck = false;
        private List<SerializableForTrans_teamInfo> list1 = null;

        public completeButtonListener(List<Team> list) {
            this.list = list;
        }

        @Override
        public void onClick(View v) {
            list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SerializableForTrans_teamInfo serializableForTransTeamInfo = new SerializableForTrans_teamInfo();
                serializableForTransTeamInfo.setTeam_name(list.get(i).getTeam_name());
                serializableForTransTeamInfo.setTeam_id(list.get(i).getTeam_id());
                serializableForTransTeamInfo.setIscheck(list.get(i).getIsChecked());
                list1.add(serializableForTransTeamInfo);
                if (list.get(i).getIsChecked()) {
                    doyoucheck = true;
                }
            }
            if (doyoucheck) {
                Intent intent = new Intent(NotifySend_Check_Activity.this, NotifySend_Activity.class);
                intent.putExtra("list", (Serializable) list1);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "请至少选择一个团队", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
