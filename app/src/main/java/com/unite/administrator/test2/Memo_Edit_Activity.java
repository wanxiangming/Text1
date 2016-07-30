package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.UserDB;
import unit.BaseActivity;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Memo_Edit_Activity extends BaseActivity {
    private int id;
    private int userid;
    private String flag;
    private UserDB userDB=null;
    private String memo=null;
    private EditText editText=null;
    private Button button=null;

    private final String update="update";
    private final String insert="insert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        Intent intent=getIntent();
        id=intent.getIntExtra("id", 0);
        flag=intent.getStringExtra("flag");
        userid=intent.getIntExtra("userid",0);
        //Log.i("memo_edit_activity",flag);
        //Log.i("memo_edit_activity", String.valueOf(id));


        editText=(EditText)findViewById(R.id.memo_activity_edit);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, inputManager.SHOW_FORCED);

        button=(Button)findViewById(R.id.memo_activity_complete_button);

        userDB=new UserDB(this);

        if(flag.equals(update)){
            memo=userDB.readmemoOne(id);
            editText.setText(memo, null);
            editText.setSelection(memo.length());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userDB.updateonememo(editText.getText().toString(), id);
                    finish();
                }
            });
        }else if(flag.equals(insert)){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String memo=editText.getText().toString();
                    if(memo.length() > 0){
                        userDB.addmemo(userid,memo);
                        finish();
                    }
                    finish();
                }
            });
        }

    }
}
