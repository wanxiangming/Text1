package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.unite.administrator.test2.Main_MessagesContext_Activity;
import com.unite.administrator.test2.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import adapter.CommentListAdapter;
import data.JsonDeal;
import listObject.Comment;
import listObject.SerializableForTrans_notifyInfo;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/18.
 */
public class Comment_Fragment extends Fragment {
    private PullToRefreshListView pullToRefreshListView=null;
    private CommentListAdapter commentListAdapter=null;
    private List<Comment> list=null;
    private int notifyid;
    private int team_id;
    private TextView textView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_comment, container, false);

        Intent intent=getActivity().getIntent();
        SerializableForTrans_notifyInfo serializableForTrans_notifyInfo= (SerializableForTrans_notifyInfo) intent.getSerializableExtra("messagesinfo");
        notifyid=serializableForTrans_notifyInfo.getNotifyId();
        team_id=serializableForTrans_notifyInfo.getTeam_id();


        textView=(TextView)view.findViewById(R.id.fragment_comment_null_textview);
        list=new ArrayList<>();
        pullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.comment_listview);
        if(list.size()==0){
            textView.setVisibility(View.VISIBLE);
            pullToRefreshListView.setVisibility(View.GONE);
        }
        commentListAdapter=new CommentListAdapter(getActivity(),R.layout.style_commentlist,list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        pullToRefreshListView.setAdapter(commentListAdapter);
        refresh();
        return view;
    }

    /**
     * 内部方法，用来获取comment，刷新listview
     */
    public void refresh(){
        GetUrl getUrl=new GetUrl();
        String u=getUrl.getNotifyComment(notifyid);
        new AsyncTask<String, Void, List<Comment>>() {
            @Override
            protected List<Comment> doInBackground(String... params) {
                List<Comment> list1 = new ArrayList<>();
                try {
                    URL url=new URL(params[0]);
                    URLConnection urlConnection=url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    InputStream in=urlConnection.getInputStream();
                    InputStreamReader inr=new InputStreamReader(in);
                    BufferedReader bur=new BufferedReader(inr);
                    if(bur.readLine().equals("1")) {
                        String string;
                        while ((string = bur.readLine()) != null) {
                            Gson gson=new Gson();
                            JsonDeal jsonDeal = gson.fromJson(string,JsonDeal.class);
                            Comment comment = new Comment();
                            comment.setMaker_name(jsonDeal.getMaker_name());
                            comment.setComment(jsonDeal.getComment());
                            list1.add(comment);
                        }
                    }
                    Thread.sleep(400);
                    bur.close();
                    inr.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return list1;
            }

            @Override
            protected void onPostExecute(List<Comment> comments) {
                super.onPostExecute(comments);
                if(comments.size() != 0){
                    textView.setVisibility(View.GONE);
                    pullToRefreshListView.setVisibility(View.VISIBLE);
                }else{
                    textView.setText("暂无评论");
                }
                commentListAdapter.changData(comments);
                pullToRefreshListView.onRefreshComplete();
            }
        }.execute(u);
    }



}
