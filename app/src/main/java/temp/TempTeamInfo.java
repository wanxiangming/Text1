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
import listObject.Team;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TempTeamInfo {
    private OnTeamInfoCallBackListener onTeamInfoCallBackListener = null;
    private Context context = null;
    private int userid;
    private JsonDeal jsonDeal = null;
    private List<Team> teamList = null;
    private GetUrl getUrl = null;
    private Boolean isConnect;

    public interface OnTeamInfoCallBackListener {
        /**
         * 该方法在UI线程中被调用，网络请求的数据已经被封装到了teamlist中
         * 其中包含teamid，teamname，isking的信息
         *
         * @param teamList
         */
        void onConnect(List<Team> teamList);

        /**
         * 该方法在没有连接上服务器的情况下被调用
         */
        void onNoConnect();
    }

    public TempTeamInfo(Context context, int userid) {
        this.context = context;
        this.userid = userid;
        getUrl = new GetUrl();
    }


    /**
     * 你可以通过这个方法获得一个team泛型的list，每一个team中包含teamid，teamname，isking的信息
     * @param onteamInfoCallBackListener
     */
    public void getInfo(OnTeamInfoCallBackListener onteamInfoCallBackListener) {
        this.onTeamInfoCallBackListener = onteamInfoCallBackListener;
        isConnect = false;
        new AsyncTask<Void, Void, JsonDeal>() {
            @Override
            protected JsonDeal doInBackground(Void... params) {
                String getDataFromServeURL = getUrl.get_team_info_from_test2(userid);
                String reader = null;
                try {
                    URL url1 = new URL(getDataFromServeURL);
                    URLConnection connection = url1.openConnection();
                    InputStream In = connection.getInputStream();
                    connection.setConnectTimeout(5000);
                    InputStreamReader Inr = new InputStreamReader(In, "utf-8");
                    BufferedReader Br = new BufferedReader(Inr);
                    reader = Br.readLine();
                    isConnect = true;
                    Br.close();
                    Inr.close();
                    In.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (reader == null) {
                    return null;
                } else {
                    Gson gson=new Gson();
                    JsonDeal jsonDeal =gson.fromJson(reader,JsonDeal.class);
                    return jsonDeal;
                }
            }

            @Override
            protected void onPostExecute(JsonDeal jsonDeal) {
                super.onPostExecute(jsonDeal);
                teamList = new ArrayList<Team>();
                if (!isConnect) {
                    onTeamInfoCallBackListener.onNoConnect();
                }
                if (jsonDeal == null) {

                } else {
                    List<Integer> list1 = jsonDeal.get_list_member_of_team();
                    List<Integer> list2 = jsonDeal.get_list_king_of_team();
                    List<String> list3 = jsonDeal.get_list_of_team_name();
                    for (int i = 0; i < list1.size(); i++) {
                        Team team = new Team();
                        team.setTeam_id(list1.get(i));
                        team.setTeam_name(list3.get(i));
                        for (int t = 0; t < list2.size(); t++) {
                            if (list1.get(i).equals(list2.get(t))) {
                                team.setIsKing();
                            }
                        }
                        teamList.add(team);     //用服务器发回来的信息，组装了一个team泛型的list
                    }
                }
                onTeamInfoCallBackListener.onConnect(teamList);
            }

        }.execute();
    }
}


