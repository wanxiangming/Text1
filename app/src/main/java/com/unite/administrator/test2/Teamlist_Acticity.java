package com.unite.administrator.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import data.JsonDeal;
import listObject.Team;
import adapter.TeamListAdapter;
import temp.TempTeamInfo;
import unit.BaseActivity;

import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/8/10.
 */
public class Teamlist_Acticity extends BaseActivity {
    private int isgetinput;
    private List<Team> list;
    private TeamListAdapter teamListAdapter;
    private PullToRefreshListView pullToRefreshListView = null;
    private Intent intent = null;
    private int userid;
    private Button callbackBT = null;
    private TextView textView = null;
    private JsonDeal jsonDeal = null;
    public static List<Team> teamList = null;
    private TempTeamInfo tempTeamInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamlist);

        intent = getIntent();
        userid = intent.getIntExtra("userid", 0);
        callbackBT = (Button) findViewById(R.id.teamlist_acticity_callback_button);
        callbackBT.setOnClickListener(new callbackBTListener());
        textView = (TextView) findViewById(R.id.teamlist_null_textview);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.teamlist_listview2);
        pullToRefreshListView.setVisibility(View.GONE);

        tempTeamInfo = new TempTeamInfo(this, userid);
        tempTeamInfo.getInfo(
                new TempTeamInfo.OnTeamInfoCallBackListener() {
                    @Override
                    public void onConnect(List<Team> teamlist) {
                        if (teamlist.size() == 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("您还未加入任何团队");
                        } else {
                            pullToRefreshListView.setVisibility(View.VISIBLE);
                            teamListAdapter = new TeamListAdapter(Teamlist_Acticity.this, R.layout.style_teamlist, teamlist);
                            pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                                @Override
                                public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                                }
                            });
                            pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int PT = position - 1;
                                    Intent inten = new Intent(Teamlist_Acticity.this, Memberlist_Activity.class);
                                    inten.putExtra("userid", userid);
                                    inten.putExtra("team_id", teamListAdapter.getItem(PT).getTeam_id());
                                    inten.putExtra("isKing", teamListAdapter.getItem(PT).IsKing());
                                    startActivity(inten);
                                }
                            });
                            pullToRefreshListView.setAdapter(teamListAdapter);

                        }
                    }

                    @Override
                    public void onNoConnect() {
                        textView.setText("请检查网络");
                    }
                });
    }


    public class callbackBTListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

}

