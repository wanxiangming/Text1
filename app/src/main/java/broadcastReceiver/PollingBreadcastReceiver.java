package broadcastReceiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import data.LocalOnlionUserInfo;
import data.UserDB;
import unit.PollingService;

/**
 * Created by Administrator on 2015/8/12.
 * 这个类用来处理轮询操作，服务器传递回来的信息，将会在程序没有打开的情况下触发通知栏显示内容
 */
public class PollingBreadcastReceiver extends BroadcastReceiver{
    private int userid;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, PollingService.class);
        context.startService(i);
    }
}
