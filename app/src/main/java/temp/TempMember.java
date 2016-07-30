package temp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import listObject.Member;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TempMember {
    private Boolean isConnect;
    private OnCallBackListener onCallBackListener;

    public interface OnCallBackListener{
        void OnConnect(List<Member> list);
        void OnNoConnect();
    }

    public TempMember(){

    }

    public void getMemberInfo(int team_id,OnCallBackListener oncallBackListener){
        GetUrl getUrl=new GetUrl();
        this.onCallBackListener=oncallBackListener;
        isConnect=false;
        new AsyncTask<String, Void, List<Member>>() {
            @Override
            protected List<Member> doInBackground(String... params) {
                List<Member>list=new ArrayList<>();
                try {
                    URL url=new URL(params[0]);
                    URLConnection urlConnection=url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    InputStream in=urlConnection.getInputStream();
                    InputStreamReader inr=new InputStreamReader(in);
                    BufferedReader bur=new BufferedReader(inr);
                    String read;
                    isConnect=true;
                    while ((read=bur.readLine()) != null){
                        Member member=new Member();
                        JSONObject jsonObject=new JSONObject(read);
                        member.setMemberId(jsonObject.getInt("member_id"));
                        member.setMemberName(jsonObject.getString("member_name"));
                        list.add(member);
                    }
                    bur.close();
                    inr.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Member> members) {
                super.onPostExecute(members);
                if(isConnect){
                    onCallBackListener.OnConnect(members);
                }else {
                    onCallBackListener.OnNoConnect();
                }
            }
        }.execute(getUrl.getMemberList(team_id));
    }
}
