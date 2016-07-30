package fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unite.administrator.test2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import listObject.SerializableForTrans_notifyInfo;
import unit.GetUrl;

/**
 * Created by Administrator on 2015/8/20.
 */
public class Comment_Edittext_Fragment extends Fragment {
    private EditText editText=null;
    private TextView textView=null;
    private Button button=null;
    private OnremoveListener onremoveListener=null;
    private int userid;
    private int notifyid;
    private FragmentManager fragmentManager=null;
    private Boolean isgetinput;


    public void setOnremove(OnremoveListener onremoveListener){
        this.onremoveListener=onremoveListener;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notify_comment_edittext, container, false);

        Intent intent=getActivity().getIntent();
        SerializableForTrans_notifyInfo serializableForTrans_notifyInfo= (SerializableForTrans_notifyInfo) intent.getSerializableExtra("messagesinfo");
        userid=serializableForTrans_notifyInfo.getUserid();
        notifyid=serializableForTrans_notifyInfo.getNotifyId();
        fragmentManager=getFragmentManager();

        editText=(EditText)view.findViewById(R.id.notify_comment_edittext);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //inputManager.showSoftInput(editText, 2);
        inputManager.toggleSoftInput(0, inputManager.SHOW_FORCED);


        textView=(TextView)view.findViewById(R.id.notify_comment_textview);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment_Edittext_Fragment comment_edittext_fragment= (Comment_Edittext_Fragment) fragmentManager.findFragmentByTag("comment_edittext");
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.remove(comment_edittext_fragment);
                fragmentTransaction.commit();
                InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onremoveListener.Onremove();
            }
        });

        button=(Button)view.findViewById(R.id.notify_comment_buttom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length()>0){
                    GetUrl geturl=new GetUrl();
                    String url=geturl.sendNotifyComment(userid, editText.getText().toString(), notifyid);
                    isgetinput=false;
                    new AsyncTask<String,Void,Void>(){
                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                URL url1=new URL(params[0]);
                                URLConnection urlConnection=url1.openConnection();
                                urlConnection.setConnectTimeout(5000);
                                InputStream in=urlConnection.getInputStream();
                                InputStreamReader inr=new InputStreamReader(in);
                                BufferedReader bur=new BufferedReader(inr);
                                isgetinput=true;
                                bur.close();
                                inr.close();
                                in.close();
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
                            if(isgetinput){
                                Comment_Fragment comment_fragment= (Comment_Fragment) fragmentManager.findFragmentByTag("comment");
                                Comment_Edittext_Fragment comment_edittext_fragment= (Comment_Edittext_Fragment) fragmentManager.findFragmentByTag("comment_edittext");
                                if(comment_fragment != null){
                                    comment_fragment.refresh();
                                }else{
                                    Comment_Fragment commentFragment=new Comment_Fragment();
                                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.animator.right_enter, R.animator.left_exit, R.animator.left_enter, R.animator.right_exit);
                                    fragmentTransaction.add(R.id.left_layout,commentFragment,"comment");
                                    fragmentTransaction.commit();
                                }
                                if(comment_edittext_fragment != null){
                                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                    fragmentTransaction.remove(comment_edittext_fragment);
                                    fragmentTransaction.commit();
                                    InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                    onremoveListener.Onremove();
                                }
                            }else{
                                Toast toust=Toast.makeText(getActivity(),"请检查网络",Toast.LENGTH_SHORT);
                                toust.setGravity(Gravity.TOP,0,0);
                                toust.show();

                            }

                        }
                    }.execute(url);
                }else{
                    Toast toust=Toast.makeText(getActivity(),"请输入内容",Toast.LENGTH_SHORT);
                    toust.setGravity(Gravity.TOP,0,0);
                    toust.show();
                }
            }

        });

        return view;
    }
}
