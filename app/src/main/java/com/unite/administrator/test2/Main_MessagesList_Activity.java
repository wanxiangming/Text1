package com.unite.administrator.test2;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.LocalOnlionUserInfo;
import data.UserDB;
import listObject.Messages;
import listObject.SerializableForTrans_notifyInfo;
import adapter.MessagesListAdapter;
import listObject.SerializableForTrans_teamInfo;
import unit.BaseActivity;

import android.view.View.OnClickListener;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2015/8/15.
 */
public class Main_MessagesList_Activity extends BaseActivity {
    private PullToRefreshListView listView = null;
    private Button callbackBT;
    private UserDB userDB = null;
    private Intent intent = null;
    private int team_id;
    private int userid;
    private List<Messages> list = null;
    private MessagesListAdapter messagesListAdapter = null;
    private UserDB userDBForUserid = null;
    private LocalOnlionUserInfo localOnlionUserInfo = null;
    private LinearLayout linearLayout;
    private Button moreBT = null;
    private PopupWindow popupWindow = null;
    private View popwind = null;

    private int startX;
    private int nowX;

    private int headHigh;
    private int width;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        intent = getIntent();
        team_id = intent.getIntExtra("team_id", 0);   //team_id userid是上一个activity给过来的
        userid = intent.getIntExtra("userid", 0);

        list = checkMessagesInfo();

        callbackBT = (Button) findViewById(R.id.messageslist_acticity_callback_button);
        callbackBT.setOnClickListener(new callbackBTListener());
        moreBT = (Button) findViewById(R.id.messageslist_acticity_more_button);
        moreBT.setOnClickListener(new moreBTListener());

        linearLayout = (LinearLayout) findViewById(R.id.messageslist_activity);

        listView = (PullToRefreshListView) findViewById(R.id.main_team_info_listview);
        messagesListAdapter = new MessagesListAdapter(this, R.layout.style_messageslist, list);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        //InfoDeal infoDeal=new InfoDeal(Main_MessagesList_Activity.this,userid);
                        //infoDeal.getAll();
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        messagesListAdapter.changeData(checkMessagesInfo());
                        listView.onRefreshComplete();
                    }
                }.execute();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int PS = position - 1;
                SerializableForTrans_notifyInfo serializableForTrans_notifyInfo = new SerializableForTrans_notifyInfo();
                serializableForTrans_notifyInfo.setTeam_id(messagesListAdapter.getItem(PS).getTeam_id());
                serializableForTrans_notifyInfo.setMaker_name(messagesListAdapter.getItem(PS).getMaker_name());
                serializableForTrans_notifyInfo.setMessages(messagesListAdapter.getItem(PS).getMessages());
                serializableForTrans_notifyInfo.setTime(messagesListAdapter.getItem(PS).getTime());
                serializableForTrans_notifyInfo.setNotifyId(messagesListAdapter.getItem(PS).getnotifyId());
                serializableForTrans_notifyInfo.setUserid(messagesListAdapter.getItem(PS).getUserid());

                userDB.changeTheTeamIsReadInfoOnNotify(team_id, userid, messagesListAdapter.getItem(PS).getnotifyId());

                Intent inten = new Intent(Main_MessagesList_Activity.this, Main_MessagesContext_Activity.class);
                inten.putExtra("messagesinfo", serializableForTrans_notifyInfo);
                startActivity(inten);
                overridePendingTransition(R.anim.right_enter, R.anim.left_exit);

                //Toast.makeText(getApplicationContext(),"点到了"+messagesListAdapter.getItem(position-1).getMaker_id(),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(messagesListAdapter);


    }


    //// TODO: 2015/8/25 当我们点进一个团队的时候，客户端与服务器做一下检查，如果发现该团队已经不存在了，那么关闭acticity，打出提示，清除本地该team信息
    private void checkForExsits(){
        
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                nowX = (int) ev.getX();
                Log.i("main", String.valueOf(nowX));
                break;
            case MotionEvent.ACTION_UP:
                if (nowX - startX > 100) {
                    finish();
                    overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
                    return true;
                }

        }
        return super.dispatchTouchEvent(ev);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * moreBT的监听器
     */
    public class moreBTListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = LayoutInflater.from(Main_MessagesList_Activity.this);
            popwind = layoutInflater.inflate(R.layout.popwind_messages_memu, null);
            popupWindow = new PopupWindow(popwind, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            Button button = (Button) popwind.findViewById(R.id.popwin_messages_memu_send_notify);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<SerializableForTrans_teamInfo> list1 = new ArrayList<>();
                    SerializableForTrans_teamInfo serializableForTransTeamInfo = new SerializableForTrans_teamInfo();
                    serializableForTransTeamInfo.setTeam_name("");
                    serializableForTransTeamInfo.setTeam_id(team_id);
                    serializableForTransTeamInfo.setIscheck(true);
                    list1.add(serializableForTransTeamInfo);
                    Intent intent = new Intent(Main_MessagesList_Activity.this, NotifySend_Activity.class);
                    intent.putExtra("list", (Serializable) list1);
                    popupWindow.dismiss();
                    startActivity(intent);
                }
            });
            View layout = findViewById(R.id.messageslist_head);
            width = layout.getWidth() - button.getWidth();
            headHigh = layout.getHeight();
            //Log.i("main", String.valueOf(layout.getHeight()));

            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(popwind, width, headHigh + 40);

        }
    }


    public class callbackBTListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Main_MessagesList_Activity.this.finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
        }
    }

    /**
     * 找出这个用户的not_info表信息
     */
    private List<Messages> checkMessagesInfo() {
        userDB = new UserDB(this);
        userDB.changeTheTeamIsReadInfoOnRequest(team_id, userid);
        List<Messages> list = userDB.getMessagesList(team_id, userid);  //这个界面的信息是根据userid在本地not_info表里查询出来的
        //userDB.closeAll();
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //userDB.changeTheTeamIsReadInfoOnNotify(team_id, userid);
        messagesListAdapter.changeData(checkMessagesInfo());
    }
}
