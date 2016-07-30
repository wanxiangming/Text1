package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import data.LocalOnlionUserInfo;
import data.UserDB;
import unit.BaseActivity;

/**
 * Created by Administrator on 2015/8/22.
 */
public class My_Acticity extends BaseActivity {
    private UserDB userDB;
    private String username;
    private LocalOnlionUserInfo localOnlionUserInfo=null;
    private TextView nameTextView=null;
    private Button nameButton=null;
    private Button callbackButton=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        userDB=new UserDB(this);
        localOnlionUserInfo=userDB.findOnlionUser();
        username=localOnlionUserInfo.getUsername();

        nameTextView=(TextView)findViewById(R.id.my_name_textview);
        nameTextView.setText(username);

        nameButton=(Button)findViewById(R.id.my_name_button);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_Acticity.this,My_change_name_Activity.class);
                startActivity(intent);
            }
        });

        callbackButton=(Button)findViewById(R.id.my_acticity_callback_button);
        callbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        localOnlionUserInfo=userDB.findOnlionUser();
        username=localOnlionUserInfo.getUsername();
        nameTextView.setText(username);
    }
}
