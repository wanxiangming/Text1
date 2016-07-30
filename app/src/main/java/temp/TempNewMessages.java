package temp;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import data.JsonDeal;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/25.
 * 这个类，从服务器读取新消息的内容，放在jsondeal泛型的list中，它可以配合write的getNewMessages方法将新消息写入本地not_info表中
 */
public class TempNewMessages {
    private OnCallBackListener onCallBackListener;
    private int userid;
    private GetUrl getUrl = null;
    private Boolean isConnect;

    public interface OnCallBackListener {
        void OnConnect(List<JsonDeal> list);

        void OnNoConnect();
    }

    public TempNewMessages(int userid) {
        this.userid = userid;
        getUrl = new GetUrl();
    }

    public void getNewMessagese(OnCallBackListener oncallBackListener) {
        this.onCallBackListener = oncallBackListener;
        isConnect = false;
        new AsyncTask<Void, Void, List<JsonDeal>>() {
            @Override
            protected List<JsonDeal> doInBackground(Void... params) {
                String url = getUrl.getNewInfo(userid);    //http://182.92.70.93/unite/index.php?r=getinfo/getnewinfo&userid=10031
                List<JsonDeal> list = new ArrayList<>();
                String read = null;
                try {
                    URL Url = new URL(url);
                    URLConnection connection = Url.openConnection();
                    connection.setConnectTimeout(5000);
                    InputStream in = connection.getInputStream();
                    InputStreamReader inr = new InputStreamReader(in, "utf-8");
                    BufferedReader bur = new BufferedReader(inr);
                    isConnect = true;
                    Gson gson=new Gson();
                    while ((read = bur.readLine()) != null) {
                        JsonDeal userinfojson =gson.fromJson(read,JsonDeal.class);
                        list.add(userinfojson);
                    }
                    bur.close();
                    inr.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<JsonDeal> list) {
                super.onPostExecute(list);
                if (!isConnect) {
                    onCallBackListener.OnNoConnect();
                } else {
                    onCallBackListener.OnConnect(list);
                }
            }
        }.execute();
    }
}
