package temp;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import data.JsonDeal;
import data.UserDB;
import data.Writer;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/25.
 * 这个方法会从服务器获取该用户的所有messages消息，放在一个JsonDeal泛型的list中，它可以配合write的getAll方法，将所有的消息写入本地not_info表中
 */
public class TempAllMessages {
    private OnCallBackListener onCallBackListener = null;
    private GetUrl getUrl = null;
    private int userid;
    private UserDB userDB=null;
    private Boolean isConnect=null;

    public interface OnCallBackListener {
        void OnConnect(List<JsonDeal> list);

        void OnNoConnect();
    }

    public TempAllMessages(int userid) {
        this.userid = userid;
        getUrl=new GetUrl();
    }


    public void getAllInfo(OnCallBackListener oncallBackListener) {
        this.onCallBackListener = oncallBackListener;
        isConnect=false;
        new AsyncTask<String, Void, List<JsonDeal>>() {
            @Override
            protected List<JsonDeal> doInBackground(String... params) {
                String url = getUrl.getAll(userid);
                List<JsonDeal> jsonDealList = new ArrayList<>();
                String read;
                try {
                    URL Url = new URL(url);
                    URLConnection connection = Url.openConnection();
                    connection.setConnectTimeout(5000);
                    InputStream in = connection.getInputStream();
                    InputStreamReader inr = new InputStreamReader(in, "utf-8");
                    BufferedReader bur = new BufferedReader(inr);
                    isConnect=true;
                    Gson gson=new Gson();
                    while ((read = bur.readLine()) != null) {
                        JsonDeal userinfojson = gson.fromJson(read,JsonDeal.class);
                        jsonDealList.add(userinfojson);
                    }
                    bur.close();
                    inr.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonDealList;
            }

            @Override
            protected void onPostExecute(List<JsonDeal> list) {
                super.onPostExecute(list);
                if(!isConnect){
                    onCallBackListener.OnNoConnect();
                }else {
                    onCallBackListener.OnConnect(list);
                }
            }
        }.execute();
    }
}

