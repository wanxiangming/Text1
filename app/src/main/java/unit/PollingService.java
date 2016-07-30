package unit;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;

import com.google.gson.Gson;
import com.unite.administrator.test2.Main_Activity;
import com.unite.administrator.test2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import broadcastReceiver.PollingBreadcastReceiver;
import data.JsonDeal;
import data.UserDB;

/**
 * Created by Administrator on 2015/8/12.
 * 这是一个后台执行的程序，我让它延时一定的时间，跑一遍代码，它会发出一个广播，用来唤醒另
 * 一段代码，这段被唤醒的代码会去和服务器交互，看看服务器上有没有和该用户相关的新消息产生
 */
public class PollingService extends Service {
    private final String appPakageName = "com.unite.administrator.test2";
    private final String request = "join_request";
    private final String newNotify = "new_notify";
    private final String refreshMain = "com.unite.administrator.test2.REFRESHUI";
    private final String Down = "com.unite.administrator.test2.DOWN";   //告诉用户他被强制下线的广播
    private final String Update = "com.unite.administrator.test2.UPDATE";   //告诉用户，他需要刷新一遍自己的数据,的广播
    private Vibrator vibrator = null;
    private Boolean received;
    private int isGetInput = 0;
    private int isRuning = 0;
    private Boolean isDown;
    private int id;
    private AlarmManager manager;
    private PendingIntent pi;
    private Intent intent1;
    private String deviceId;
    private GetDeviceId getDeviceId = null;
//// TODO: 2015/8/24 使用一个广播，告诉用户他需要刷新一遍自己的数据，然后在这个广播接收器中，实现数据刷新逻辑
    //先通过广播

    /**
     * 是否是请求消息的标志位
     */
    private int isRequest = 0;
    private int isNewNotify = 0;

    /**
     * 是否是通知消息的标志位
     */
    private int isnotification = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        id = Main_Activity.userid;
        getDeviceId = new GetDeviceId(this);
        deviceId = getDeviceId.getId();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //开启一个时钟，在指定时间后，打开PollingBreadcastReceiver,这个类会再次启动本service
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int fiveSecond = 5 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + fiveSecond;
        intent1 = new Intent(this, PollingBreadcastReceiver.class);
        pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        received = false;
        isDown = false;
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                GetUrl u = new GetUrl();
                String s = u.polling(id, deviceId);

                //这段代码用来执行网络请求
                isRequest = 0;    //判断服务器返回的消息标识，是不是加入团队请求
                isNewNotify = 0;
                isGetInput = 0;  //判断是否和服务器有正常的网络连接，也就是看看有没有输入流
                isRuning = 0;     //看看应用程序是否在前台运行
                isDown = false;
                URL url = null;
                try {
                    url = new URL(s);
                    URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(3000);
                    InputStream in = connection.getInputStream();
                    isGetInput = 1;      //没有网络的时候，此段以后的代码是不会执行到的
                    InputStreamReader inr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(inr);
                    String string;
//                    string = br.readLine();
                    while ((string = br.readLine()) != null) {
                        if (string.equals("down")) {      //判断一下，如果服务器发回的数据是 down，那么表示该用户已经被down机了
                            manager.cancel(pi);         //如果该用户已经被down机，那么就停止轮询
                            isDown = true;
                        } else if (string.equals("up")) {      //如果我们读到了up指令，说明该用户可以开始识别信息的内容，也就是是否需要触发notification
                            while ((string = br.readLine()) != null) {   //用户符合轮询条件，则循环取出后面的数据，视情况触发notification
                                received = true;
                                Gson gson=new Gson();
                                JsonDeal jsondeal =gson.fromJson(string,JsonDeal.class);
                                if (request.equals(jsondeal.getFlag())) {
                                    isRequest = 1;
                                } else if (newNotify.equals(jsondeal.getFlag())) {
                                    isNewNotify = 1;
                                }
                            }
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isGetInput == 1) {

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isTopActivity()) {
                    Intent in = new Intent(refreshMain);
                    sendBroadcast(in);
                    if (isDown) {
                        Intent intent2 = new Intent(Down);    //向系统发出一条Down机广播
                        sendBroadcast(intent2);
                    }
                } else {
                    if (isDown) {
                        UserDB userDB = new UserDB(PollingService.this);
                        userDB.onlionstat(id, 0);
                        stopSelf();
                    }
                    if (isRequest == 1) {
                        Intent intent1 = new Intent(PollingService.this, Main_Activity.class);
                        PendingIntent pendingIntent1 = PendingIntent.getActivity(PollingService.this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new Notification(R.drawable.groups7, "您有新请求", System.currentTimeMillis());
                        notification.setLatestEventInfo(PollingService.this, "团队", "您有新请求", pendingIntent1);
                        notificationManager.notify(1, notification);     //注意，这个1是notification的标志位，在其他地方想要cancel掉它，就需要这个标志位
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {100, 400, 100, 400};            // 停止 开启 停止 开启
                        vibrator.vibrate(pattern, -1);
                    }
                    if (isNewNotify == 1) {
                        Intent intent2 = new Intent(PollingService.this, Main_Activity.class);
                        PendingIntent pendingIntent2 = PendingIntent.getActivity(PollingService.this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification1 = new Notification(R.drawable.groups7, "您有新消息", System.currentTimeMillis());
                        notification1.setLatestEventInfo(PollingService.this, "团队", "您有新消息", pendingIntent2);
                        notificationManager.notify(2, notification1);
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                        vibrator.vibrate(pattern, -1);
                    }
                }
            }
        }.execute();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(pi);
    }

    private boolean isTopActivity() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            //应用程序位于堆栈的顶层
            if (appPakageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}

