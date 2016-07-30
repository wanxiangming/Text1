package com.unite.administrator.test2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import data.JsonDeal;
import data.Request;
import unit.GetUrl;

import static data.ToMd5.stringToMD5;

/**
 * Created by Administrator on 2015/8/17.
 */
public class Register_Activity extends Activity {
    private Button callbackBT;
    private EditText nameET;
    private EditText passET;
    private EditText aconET;
    private Button completeBT;
    private String name;
    private String acon;
    private String pass;
    private Request request = null;
    private Gson gson = null;
    private String json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        callbackBT = (Button) findViewById(R.id.register_acticity_callback_button);
        completeBT = (Button) findViewById(R.id.register_BT_register);
        nameET = (EditText) findViewById(R.id.register_ET_username);
        passET = (EditText) findViewById(R.id.register_ET_pass);
        aconET = (EditText) findViewById(R.id.register_ET_acon);

        callbackBT.setOnClickListener(new callbackButtonListener());
        completeBT.setOnClickListener(new completeButton());
    }


    public class callbackButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }


    public class completeButton implements OnClickListener {
        private int isrigester;
        private int isgetinput;
        private String dontrigester = "0";

        @Override
        public void onClick(View v) {
            GetUrl getUrl = new GetUrl();
            String url = getUrl.getloninURL();

            name = nameET.getText().toString();
            acon = aconET.getText().toString();
            pass = passET.getText().toString();
            gson = new Gson();
            request = new Request();
            request.setUsername(getUrl.encoder(name));
            request.setAccon(acon);
            request.setPassword(stringToMD5(pass));
            request.setFlag("register");
            json = gson.toJson(request);

            if (name.length() == 0 & acon.length() == 0 & pass.length() == 0) {
                Toast.makeText(getApplicationContext(), "请填写信息", Toast.LENGTH_SHORT).show();
            } else if (name.length() > 10) {
                Toast.makeText(getApplicationContext(), "名字长度不能超过10个字符", Toast.LENGTH_SHORT).show();
            } else if (acon.length() < 4 & acon.length() > 15) {
                Toast.makeText(getApplicationContext(), "账号错误，请输入4-15个长度的英文和字母", Toast.LENGTH_SHORT).show();
            } else if (pass.length() < 4) {
                Toast.makeText(getApplicationContext(), "密码至少为4个字符", Toast.LENGTH_SHORT).show();
            } else {
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (isrigester == 0 && isgetinput == 1) {
                            Toast.makeText(getApplicationContext(), "此账号已存在", Toast.LENGTH_SHORT).show();
                        } else if (isrigester == 1 && isgetinput == 1) {
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            Register_Activity.this.finish();
                        } else if (isgetinput == 0) {
                            Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url1 = new URL(params[0]);
                            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
                            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                            BufferedWriter bw = new BufferedWriter(osw);
                            bw.write(json);
                            bw.flush();

                            //Log.i("json", json);
                            InputStream In = connection.getInputStream();
                            InputStreamReader Inr = new InputStreamReader(In, "utf-8");
                            BufferedReader Br = new BufferedReader(Inr);
                            String reader = Br.readLine();
                            JsonDeal jsonDeal = gson.fromJson(reader, JsonDeal.class);
                            isgetinput = 1;
                            if (jsonDeal.getIsRegister() == 0) {
                                isrigester = 0;
                            } else {
                                isrigester = 1;
                            }
                            In.close();
                            Inr.close();
                            Br.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(url);
            }
        }
    }
}
