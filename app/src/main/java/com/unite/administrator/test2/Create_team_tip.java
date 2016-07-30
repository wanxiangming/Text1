package com.unite.administrator.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import data.JsonDeal;
import unit.BaseActivity;

/**
 * Created by Administrator on 2015/8/9.
 */
public class Create_team_tip extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_creat_team_succese);

//        Toast.makeText(getApplicationContext(),"团队创建成功",Toast.LENGTH_LONG).show();

        Intent intent=getIntent();
        String string=intent.getStringExtra("teaminfo");
        Gson gson=new Gson();
        JsonDeal JsonDeal =gson.fromJson(string, data.JsonDeal.class);

        TextView textView=(TextView)findViewById(R.id.creat_team_succese_tip_textview1);
        TextView textView1=(TextView)findViewById(R.id.creat_team_succese_tip_textview2);
        textView1.setText("团队名："+ JsonDeal.getTeam_name());
        textView.setText("团队ID："+ JsonDeal.getTeam_id());

        Button button=(Button)findViewById(R.id.creat_team_tip_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_team_tip.this.finish();
            }
        });
    }
}
