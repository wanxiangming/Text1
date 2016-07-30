package com.unite.administrator.test2;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import adapter.MemoListAdapter;
import adapter.OpenActivity;
import data.UserDB;
import fragment.Memo_Choose_Fragment;
import listObject.Memo;
import unit.BaseActivity;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Memo_Activity extends BaseActivity {
    private ListView listView = null;
    private MemoListAdapter memoListAdapter = null;
    private List<Memo> list = null;
    private UserDB userDB = null;
    private int userid;
    private Button addBT = null;
    private Button callbackButton=null;
    private View popview = null;
    public int id;
    private View popwind = null;
    private PopupWindow popupWindow = null;
    private FragmentManager fragmentManager = null;
    private final String addmemo = "insert";

//// TODO: 2015/8/22 顶部添加按钮改为一个+号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);

        callbackButton=(Button)findViewById(R.id.memo_activity_top_button1);
        callbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userDB = new UserDB(this);
        list = userDB.readmemo(userid);

        addBT = (Button) findViewById(R.id.memo_top_button);
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Memo_Activity.this, Memo_Edit_Activity.class);
                in.putExtra("userid", userid);
                in.putExtra("flag", addmemo);
                startActivity(in);
            }
        });

        listView = (ListView) findViewById(R.id.memo_list);
        memoListAdapter = new MemoListAdapter(this, R.layout.style_memolist, list, userid);
        memoListAdapter.OnOpen(new OpenActivity() {
            @Override
            public void Openactivity(int id) {
                Intent intent1 = new Intent(Memo_Activity.this, Memo_Edit_Activity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("flag", "update");
                startActivity(intent1);
//                fragmentManager=getFragmentManager();
//                FragmentTransaction transaction=fragmentManager.beginTransaction();
//                Memo_Choose_Fragment memo_choose_fragment=new Memo_Choose_Fragment();
//                transaction.add(R.id.memo_activity,memo_choose_fragment,"memo_fragment");
//                transaction.commit();
            }
        });
//        memoListAdapter.setOnclick(new MemoListAdapter.OnBTClick() {
//            @Override
//            public void OnClick(int id1) {
//                id=id1;
//                Memo_Choose_Fragment memo_choose_fragment = new Memo_Choose_Fragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.memo_activity, memo_choose_fragment, "choose");
//                fragmentTransaction.commit();
//                Log.i("memo", String.valueOf(id));
//            }
//        });
        listView.setAdapter(memoListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.memo_textview);
                textView.setText(memoListAdapter.getItem(position).getContext());
                textView.setMaxLines(150);
//                Button button=(Button)view.findViewById(R.id.delete_memo_button);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i("memo","button");
//                    }
//                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDB = new UserDB(this);
        list = userDB.readmemo(userid);
        memoListAdapter.changdate(list);
    }
}
