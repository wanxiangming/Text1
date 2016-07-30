package adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.unite.administrator.test2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import data.JsonDeal;
import data.UserDB;
import data.Writer;
import listObject.Messages;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/15.
 */
public class MessagesListAdapter extends ArrayAdapter<Messages> {
    private List<Messages> messages = null;
    private int textViewId;
    private TextView joinRequest_textView = null;
    private TextView notify_textView = null;
    private TextView notify_time_textview = null;
    private Button agreeButton = null;
    private Button ignoreButton = null;
    private LinearLayout joinRequest_linearLayout = null;
    private LinearLayout notify_linearLayout = null;
    private ImageView imageView = null;

    private final String joinRequestFlag = "join_request";
    private final String notifyFlag = "notify";

    public MessagesListAdapter(Context context, int resource, List<Messages> objects) {
        super(context, resource, objects);
        this.messages = objects;
        this.textViewId = resource;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Messages getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(this.textViewId, null);
        } else {
            view = convertView;
        }
        //view=LayoutInflater.from(getContext()).inflate(this.textViewId, null);
        Messages messages = getItem(position);
        notify_linearLayout = (LinearLayout) view.findViewById(R.id.messageslist_notify_item_layout);
        joinRequest_linearLayout = (LinearLayout) view.findViewById(R.id.messageslist_joinrequest_item_layout);
        if (messages.getFlag().equals(joinRequestFlag)) {
            joinRequest_textView = (TextView) view.findViewById(R.id.messageslist_textview_item);
            joinRequest_textView.setText("加入请求：" + messages.getMaker_name());
            agreeButton = (Button) view.findViewById(R.id.joinrequest_agree_button);
            ignoreButton = (Button) view.findViewById(R.id.joinrequest_ignore_button);
            agreeButton.setOnClickListener(new agreeButtonOncklickListener(position));
            ignoreButton.setOnClickListener(new ignoreButtonOncklickListener(position));
            notify_linearLayout.setVisibility(View.GONE);
            joinRequest_linearLayout.setVisibility(View.VISIBLE);
        } else if (messages.getFlag().equals(notifyFlag)) {
            imageView=(ImageView)view.findViewById(R.id.notify_imageview_item);
            notify_textView = (TextView) view.findViewById(R.id.notify_textview);
            notify_textView.setText(messages.getMessages());
            notify_time_textview = (TextView) view.findViewById(R.id.notify_time_textview);
            notify_time_textview.setText(messages.getTime());
            joinRequest_linearLayout.setVisibility(View.GONE);
            notify_linearLayout.setVisibility(View.VISIBLE);
            if(messages.getIsread()==1){
                imageView.setImageResource(R.drawable.list_view_tip);
            }else{
                imageView.setImageDrawable(null);
            }

        }
        return view;
    }


    public void changeData(List<Messages> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }


    public class ignoreButtonOncklickListener implements OnClickListener {
        private int isconnect = 0;
        private int team_id;
        private int maker_id;
        private int userid;
        private int position;

        public ignoreButtonOncklickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            GetUrl geturl = new GetUrl();
            String string = geturl.agreeJoinRequest(getItem(position).getTeam_id(), getItem(position).getMaker_id(), "ignore");
            new AsyncTask<String, Void, Void>() {

                @Override
                protected Void doInBackground(String... params) {
                    //JsonDeal jsonDeal=null;
                    try {
                        URL url1 = new URL(params[0]);
                        URLConnection urlConnection = url1.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        InputStream in = urlConnection.getInputStream();
                        InputStreamReader inr = new InputStreamReader(in, "utf-8");
                        BufferedReader bur = new BufferedReader(inr);
                        isconnect = 1;
                        bur.close();
                        inr.close();
                        in.close();
                        //InfoDeal infoDeal=new InfoDeal(getContext(),userid);
                        //jsonDeal=infoDeal.updateLocalTeamInfo();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (isconnect == 1) {
                        UserDB userDB = new UserDB(getContext());
                        maker_id = getItem(position).getMaker_id();
                        team_id = getItem(position).getTeam_id();
                        userid = getItem(position).getUserid();
                        userDB.deleteOneJoinRequest(userid, team_id, maker_id);
                        //Log.i("info", "maker_id=" + String.valueOf(maker_id) + "  userid=" + String.valueOf(userid) + "  team_id=" + String.valueOf(team_id));
                        List<Messages> messages1 = userDB.getMessagesList(team_id, userid);
                        //userDB.closeAll();
                        messages.clear();
                        messages.addAll(messages1);
                        notifyDataSetChanged();
                        //Toast.makeText(getContext(),"新成员已加入",Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(string);
        }
    }


    public class agreeButtonOncklickListener implements OnClickListener {
        private int position;
        private int isconnect = 0;
        private int team_id;
        private int maker_id;
        private int userid;

        public agreeButtonOncklickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            GetUrl url = new GetUrl();
            String string = url.agreeJoinRequest(getItem(position).getTeam_id(), getItem(position).getMaker_id(), "agree");
            new AsyncTask<String, Void, JsonDeal>() {
                @Override
                protected JsonDeal doInBackground(String... params) {
                    JsonDeal jsonDeal = null;
                    try {
                        URL url1 = new URL(params[0]);
                        URLConnection urlConnection = url1.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        InputStream in = urlConnection.getInputStream();
                        InputStreamReader inr = new InputStreamReader(in, "utf-8");
                        BufferedReader bur = new BufferedReader(inr);
                        isconnect = 1;
                        bur.close();
                        inr.close();
                        in.close();
//                        InfoDeal infoDeal = new InfoDeal(getContext(), userid);
//                        jsonDeal = infoDeal.updateLocalTeamInfo();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return jsonDeal;
                }

                @Override
                protected void onPostExecute(JsonDeal jsonDeal) {
                    super.onPostExecute(jsonDeal);
                    if (isconnect == 1) {
                        UserDB userDB = new UserDB(getContext());
                        maker_id = getItem(position).getMaker_id();
                        team_id = getItem(position).getTeam_id();
                        userid = getItem(position).getUserid();
//                        Writer writer = new Writer(userDB, userid);
//                        writer.updateLocalTeamInfo(jsonDeal);
                        userDB.deleteOneJoinRequest(userid, team_id, maker_id);
                        //Log.i("info", "maker_id=" + String.valueOf(maker_id) + "  userid=" + String.valueOf(userid) + "  team_id=" + String.valueOf(team_id));
                        List<Messages> messages1 = userDB.getMessagesList(team_id, userid);
                        messages.clear();
                        messages.addAll(messages1);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "新成员已加入", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(string);
        }
    }
}
