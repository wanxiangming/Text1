package com.unite.administrator.test2;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import broadcastReceiver.Down_Receiver;
import data.JsonDeal;
import data.Request;
import data.Writer;
import listObject.Team;
import data.LocalOnlionUserInfo;
import data.UserDB;
import temp.TempAllMessages;
import temp.TempNewMessages;
import unit.BaseActivity;
import unit.PollingService;
import adapter.TeamListAdapter;

/**
 * Created by Administrator on 2015/8/1.
 */
public class Main_Activity extends BaseActivity {
    private Button main_top_button = null;
    private Button popwind_create_button = null;
    private Button popwind_logout_button = null;
    private Button popwind_team_button = null;
    private Button popwind_join_button = null;
    private Button popwind_sendnotify_button = null;
    private Button popwind_my_button = null;
    private Button memoBT = null;
    private TextView textView = null;
    private PullToRefreshListView pullToRefreshListView = null;
    private PopupWindow popupWindow = null;
    private View popview = null;
    private List<Team> list = null;
    private UserDB userDB = null;
    private TeamListAdapter teamListAdapter = null;
    private RefreshBroadcastiReceiver refreshbroadcastireceiver = null;
    private IntentFilter intentFilter = null;
    private Down_Receiver down_receiver = null;
    private TextView imageview = null;
    private TempAllMessages tempAllMessages = null;
    private TempNewMessages tempNewMessages = null;

    public static int userid;   //userid已经设置为一个静态变量了
    private final String refreshUI = "com.unite.administrator.test2.REFRESHUI";
    private final String Down = "com.unite.administrator.test2.DOWN";

    /**
     * 屏幕宽高
     */
    int activity_width;
    int activity_height;

    int top_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取登陆者ID
        userDB = new UserDB(this);
        LocalOnlionUserInfo localOnlionUserInfo = userDB.findOnlionUser();
        userid = localOnlionUserInfo.getId();

        //更新顶端显示的id
        textView = (TextView) findViewById(R.id.main_activity_top_textview);
//        String s = String.valueOf(userid);
        textView.setText(String.valueOf(userid));
        Log.i("userid",String.valueOf(userid));

        //这个intent会开启pollingservice
        Intent i = new Intent(this, PollingService.class);
        startService(i);


        //取得屏幕高宽
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        activity_width = metric.widthPixels;
        activity_height = metric.heightPixels;

        //设置顶部按钮监听器
        main_top_button = (Button) findViewById(R.id.main_top_button);
        main_top_button.setOnClickListener(new main_top_button_listener());

        memoBT = (Button) findViewById(R.id.memo_button);
        memoBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Memo_Activity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });


        //设置消息的listview
        list = userDB.getLimitTeamList(userid);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.main_list);
        teamListAdapter = new TeamListAdapter(this, R.layout.style_teamlist, list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                tempNewMessages = new TempNewMessages(userid);
                tempNewMessages.getNewMessagese(new TempNewMessages.OnCallBackListener() {
                    @Override
                    public void OnConnect(List<JsonDeal> list) {
                        Writer writer = new Writer(userDB, userid);
                        writer.getNewMessages(list);
                        List<Team> list2 = userDB.getLimitTeamList(userid);
                        teamListAdapter.changeData(list2);
                        pullToRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void OnNoConnect() {

                    }
                });
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int PK = position - 1;
                Intent intent = new Intent(Main_Activity.this, Main_MessagesList_Activity.class);
                intent.putExtra("team_id", teamListAdapter.getItem(PK).getTeam_id());
                intent.putExtra("userid", userid);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
            }
        });
        pullToRefreshListView.setAdapter(teamListAdapter);


        //关闭notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        notificationManager.cancel(2);

        imageview = (TextView) findViewById(R.id.main_imageview);
        init();
    }


    /**
     * 初始化界面
     */
    private void init() {
        Intent intent = getIntent();
        Boolean isFromLogin = intent.getBooleanExtra("login", false);
        List<JsonDeal> list = null;
        if (isFromLogin) {
            tempAllMessages = new TempAllMessages(userid);
            tempAllMessages.getAllInfo(new TempAllMessages.OnCallBackListener() {
                @Override
                public void OnConnect(List<JsonDeal> list) {
                    Writer writer = new Writer(userDB, userid);
                    writer.getAll(list);
                    tempNewMessages = new TempNewMessages(userid);
                    tempNewMessages.getNewMessagese(new TempNewMessages.OnCallBackListener() {
                        @Override
                        public void OnConnect(List<JsonDeal> list) {
                            Writer writer = new Writer(userDB, userid);
                            writer.getNewMessages(list);
                            List<Team> list1 = userDB.getLimitTeamList(userid);
                            teamListAdapter.changeData(list1);
                            if (list1.size() == 0) {
                                pullToRefreshListView.setVisibility(View.GONE);
                                imageview.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void OnNoConnect() {

                        }
                    });
                }

                @Override
                public void OnNoConnect() {
                    Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //list = new ArrayList<JsonDeal>();
        }
        intentFilter = new IntentFilter();
        intentFilter.addAction(refreshUI);
        refreshbroadcastireceiver = new RefreshBroadcastiReceiver();
        registerReceiver(refreshbroadcastireceiver, intentFilter);
    }


    /**
     * 这个广播接收器，会在软件处于栈顶的情况下，跟着轮询服务一起心跳，从服务器实时的读取数据，它将会在Main_Acticity被destroy的时候关闭
     */
    private class RefreshBroadcastiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            tempNewMessages = new TempNewMessages(userid);
            tempNewMessages.getNewMessagese(new TempNewMessages.OnCallBackListener() {
                @Override
                public void OnConnect(List<JsonDeal> list) {
                    if (list.size() != 0) {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {100, 250, 100, 250};   // 停止 开启 停止 开启
                        vibrator.vibrate(pattern, -1);
                    }
                    Writer writer = new Writer(userDB, userid);
                    writer.getNewMessages(list);
                    List<Team> list1 = userDB.getLimitTeamList(userid);    //刷新首页的listview
                    teamListAdapter.changeData(list1);
                }

                @Override
                public void OnNoConnect() {

                }
            });
        }
    }


    private class main_top_button_listener implements OnClickListener {
        @Override
        public void onClick(View v) {

            View layout = findViewById(R.id.main_top_layout);
            top_height = layout.getHeight();

            LayoutInflater layoutInflater = LayoutInflater.from(Main_Activity.this);
            popview = layoutInflater.inflate(R.layout.pupwind_main_menu, null);
            popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            //创建 按钮的监听器
            popwind_create_button = (Button) popview.findViewById(R.id.popwind_mune_create);
            popwind_create_button.setOnClickListener(new popwind_create_button_listener());

            //登出 按钮监听器
            popwind_logout_button = (Button) popview.findViewById(R.id.popwind_menu_logout);
            popwind_logout_button.setOnClickListener(new popwind_logout_button_listener());

            //团队 按钮监听器
            //// TODO: 2015/8/11 团队列表界面的返回键 ,团队列表的美化
            popwind_team_button = (Button) popview.findViewById(R.id.popwin_menu_team);
            popwind_team_button.setOnClickListener(new popwin_team_list_button_listener());


            //加入 按钮监听器
            popwind_join_button = (Button) popview.findViewById(R.id.popwin_menu_join);
            popwind_join_button.setOnClickListener(new popwind_join_button_listener());


            //发通知 按钮监听器
            popwind_sendnotify_button = (Button) popview.findViewById(R.id.popwin_menu_sendnotify);
            popwind_sendnotify_button.setOnClickListener(new popwind_sendnotify_button_listener());

            //我 按钮监听器
            popwind_my_button = (Button) popview.findViewById(R.id.popwind_menu_my);
            popwind_my_button.setOnClickListener(new popwind_my_button_listener());

            popupWindow.setOutsideTouchable(true);
            popupWindow.setHeight(activity_height - top_height - 38);
            popupWindow.setWidth(activity_width);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(popview, 0, top_height + 38);


        }

    }


    public class popwind_my_button_listener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Main_Activity.this, My_Acticity.class);
            startActivity(intent);
        }
    }


    public class popwind_sendnotify_button_listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Main_Activity.this, NotifySend_Check_Activity.class);
            intent.putExtra("userid", userid);
            popupWindow.dismiss();
            startActivity(intent);
        }
    }


    /*
    加入按钮的监听器
     */
    public class popwind_join_button_listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Main_Activity.this, Join_Activity.class);
            startActivity(intent);
        }
    }


    /*
    团队列表按钮的监听器
     */
    public class popwin_team_list_button_listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Main_Activity.this, Teamlist_Acticity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
        }
    }


    /*
    创建团队按钮的监听器
     */
    public class popwind_create_button_listener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(Main_Activity.this, Create_Team_Activity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
        }
    }


    /*
    登出按钮的监听器
     */
    public class popwind_logout_button_listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Main_Activity.this, Login_Activity.class);
            LocalOnlionUserInfo localOnlionUserInfo = userDB.findOnlionUser();
            userDB.onlionstat(localOnlionUserInfo.getId(), 0);
            startActivity(intent);
            Main_Activity.this.finish();
            Intent intent1 = new Intent(Main_Activity.this, PollingService.class);
            stopService(intent1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //userDB.closeAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDB = new UserDB(this);
        imageview.setVisibility(View.GONE);
        pullToRefreshListView.setVisibility(View.VISIBLE);
        list = userDB.getLimitTeamList(userid);
        teamListAdapter.changeData(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDB.closeAll();
        unregisterReceiver(refreshbroadcastireceiver);
    }
}
