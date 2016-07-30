package com.unite.administrator.test2;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import data.UserDB;
import fragment.Comment_Edittext_Fragment;
import fragment.Comment_Fragment;
import fragment.NotifyContext_Fragment;
import listObject.SerializableForTrans_notifyInfo;
import unit.BaseActivity;
import unit.MyAnimation;
import fragment.OnremoveListener;

/**
 * Created by Administrator on 2015/8/18.
 */
public class Main_MessagesContext_Activity extends BaseActivity {
    private Button topCallBackBT = null;
    private Button addToMemoBT = null;
    private Button commentBT = null;
    private ScrollView scrollView = null;
    private TextView notifyTextView = null;
    private ListView listView = null;
    private Button editbutton = null;
    private int width;
    private MyAnimation animation = null;
    private View popwin = null;
    private PopupWindow popupWindow = null;
    private EditText editText = null;
    private TextView notify_comment_textview = null;
    private TextView notify_comment_textview1 = null;
    private Button notify_comment_buttom = null;
    private UserDB userDB = null;


    private NotifyContext_Fragment notifyContext_fragment = null;
    private Comment_Fragment comment_fragment = null;
    private FragmentManager fragmentManager = null;
    private FrameLayout frameLayout = null;
    private IntentFilter intentFilter = null;
    private Broadcastreceiver broadcastreceiver = null;
    private final String refreshMain = "com.unite.administrator.test2.REFRESHUI";

    private int userid;
    private int notifyid;
    private String comment;
    private int startX;
    private int endX;
    private String notifyinfo;

    //// TODO: 2015/8/21 加一个滑动finish
    //// TODO: 2015/8/21 comment为空时，显示内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_context);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        Intent intent = getIntent();
        SerializableForTrans_notifyInfo serializableForTrans_notifyInfo = (SerializableForTrans_notifyInfo) intent.getSerializableExtra("messagesinfo");
//        Comment_Fragment fragment_comment=(Comment_Fragment)getFragmentManager().findFragmentById(R.id.fragment_comment);
//        fragment_comment.setTextView(serializableForTrans_notifyInfo.getMessages());
        notifyid = serializableForTrans_notifyInfo.getNotifyId();
        userid = serializableForTrans_notifyInfo.getUserid();
        notifyinfo = serializableForTrans_notifyInfo.getMessages();

        editbutton = (Button) findViewById(R.id.comment_edit_button);
        editbutton.setOnClickListener(new eidttextListener());

        scrollView = (ScrollView) findViewById(R.id.notify_context_scrllview);
        topCallBackBT = (Button) findViewById(R.id.messages_context_acticity_callback_button);
        topCallBackBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            }
        });

        addToMemoBT = (Button) findViewById(R.id.messages_context_activity_addmemo_button);
        addToMemoBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userDB = new UserDB(Main_MessagesContext_Activity.this);
                userDB.addmemo(userid, notifyinfo);
                Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_SHORT).show();
            }
        });

        commentBT = (Button) findViewById(R.id.messages_context_acticity_comment_button);
        commentBT.setOnClickListener(new commentBTListener());

        fragmentManager = getFragmentManager();
        notifyContext_fragment = (NotifyContext_Fragment) fragmentManager.findFragmentById(R.id.left_fragment);
        frameLayout = (FrameLayout) findViewById(R.id.left_layout);

        intentFilter = new IntentFilter();
        intentFilter.addAction(refreshMain);
        broadcastreceiver = new Broadcastreceiver();
        registerReceiver(broadcastreceiver, intentFilter);
    }


    public class eidttextListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Comment_Edittext_Fragment comment_edittext_fragment = new Comment_Edittext_Fragment();
            comment_edittext_fragment.setOnremove(new OnremoveListener() {
                @Override
                public void Onremove() {
                    editbutton.setVisibility(View.VISIBLE);
                    commentBT.setVisibility(View.VISIBLE);
                }
            });
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.left_layout, comment_edittext_fragment, "comment_edittext");
            transaction.commit();
            editbutton.setVisibility(View.GONE);
            commentBT.setVisibility(View.GONE);
        }
    }


    private class Broadcastreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Comment_Fragment comment_fragment = (Comment_Fragment) fragmentManager.findFragmentByTag("comment");
            if (comment_fragment != null) {
                comment_fragment.refresh();
            }
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (endX - startX > 150) {
                    finish();
                    overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
                    return true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastreceiver);
    }

    public class commentBTListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (fragmentManager.findFragmentByTag("comment") == null) {
                //NotifyContext_Fragment notifyContext_fragment= (NotifyContext_Fragment) fragmentManager.findFragmentById(R.id.notifyContext_fragment);
                //fragment_comment=(Comment_Fragment)fragmentManager.findFragmentById(R.id.fragment_comment);
                //transaction.setCustomAnimations(R.anim.right_enter,R.anim.left_exit);
                //transaction.setTransitionStyle(R.anim.right_enter);
                Fragment right_fragment = new Comment_Fragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.animator.right_enter, R.animator.left_exit, R.animator.left_enter, R.animator.right_exit);
                transaction.add(R.id.left_layout, right_fragment, "comment");
                transaction.commit();
                commentBT.setText("原文");
            } else {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.animator.right_enter, R.animator.right_exit, R.animator.left_enter, R.animator.right_exit);
                transaction.remove(fragmentManager.findFragmentByTag("comment"));
                transaction.commit();
                commentBT.setText("评论");
            }
        }
    }
}

//            LayoutInflater layoutInflater=LayoutInflater.from(Main_MessagesContext_Activity.this);
//            popwin=layoutInflater.inflate(R.layout.fragment_notify_comment_edittext,null);
//            popupWindow=new PopupWindow(popwin, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
//
//
//            popupWindow.setOutsideTouchable(true);
//            //popupWindow.setHeight(activity_height - top_height - 40);
//            //popupWindow.setWidth(activity_width);
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//            popupWindow.showAsDropDown(popwin, 0, 0);
//            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    editbutton.setVisibility(View.VISIBLE);
//                    commentBT.setVisibility(View.VISIBLE);
//                }
//            });
//
//            editText=(EditText)popwin.findViewById(R.id.notify_comment_edittext);
//            editText.setFocusable(true);
//            editText.setFocusableInTouchMode(true);
//            editText.requestFocus();
//            InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            //inputManager.showSoftInput(editText, 2);
//            inputManager.toggleSoftInput(0, inputManager.SHOW_FORCED);
//
//            notify_comment_textview=(TextView)popwin.findViewById(R.id.notify_comment_textview);
//            notify_comment_textview.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
////            notify_comment_textview1=(TextView)popwin.findViewById(R.id.notify_comment_textview1);
////            notify_comment_textview1.setOnClickListener(new OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    popupWindow.dismiss();
////                }
////            });
//
//            notify_comment_buttom=(Button)popwin.findViewById(R.id.notify_comment_buttom);
//            notify_comment_buttom.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(editText.getText().length()>0){
//                        GetUrl geturl=new GetUrl();
//                        String url=geturl.sendNotifyComment(userid, editText.getText().toString(), notifyid);
//                        new AsyncTask<String,Void,Void>(){
//                            @Override
//                            protected Void doInBackground(String... params) {
//                                try {
//                                    URL url1=new URL(params[0]);
//                                    URLConnection urlConnection=url1.openConnection();
//                                    urlConnection.setConnectTimeout(5000);
//                                    InputStream in=urlConnection.getInputStream();
//                                    InputStreamReader inr=new InputStreamReader(in);
//                                    BufferedReader bur=new BufferedReader(inr);
//
//                                    bur.close();
//                                    inr.close();
//                                    in.close();
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Void aVoid) {
//                                super.onPostExecute(aVoid);
//                                Comment_Fragment comment_fragment= (Comment_Fragment) fragmentManager.findFragmentByTag("comment");
//                                if(comment_fragment != null){
//                                    comment_fragment.refresh();
//                                }
//                                if(popupWindow.isShowing()){
//                                    popupWindow.dismiss();
//                                    if(fragmentManager.findFragmentByTag("comment")==null){
//                                        commentBT.performClick();
//                                    }
//                                }
//                            }
//                        }.execute(url);
//                    }else{
//                        Toast.makeText(getApplicationContext(),"请输入内容",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            editbutton.setVisibility(View.GONE);
//            commentBT.setVisibility(View.GONE);