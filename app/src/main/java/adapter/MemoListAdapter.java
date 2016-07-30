package adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.unite.administrator.test2.Memo_Edit_Activity;
import com.unite.administrator.test2.R;

import java.util.List;

import data.UserDB;
import listObject.Memo;

/**
 * Created by Administrator on 2015/8/21.
 */
public class MemoListAdapter extends ArrayAdapter<Memo> {
    private int resourceID;
    private List<Memo> list=null;
    private TextView textView1=null;
    private Button button=null;
    private int id;
    private Context context=null;
    private int userid;

    private View popwin=null;
    private PopupWindow popupWindow=null;
    private Button deleteBT=null;
    private Button editBT=null;
    private EditText editText=null;
    private TextView textView=null;

    private OpenActivity openActivity;

    public MemoListAdapter(Context context, int resource, List<Memo> objects,int userid) {
        super(context, resource, objects);
        this.resourceID=resource;
        this.list=objects;
        this.context=context;
        this.userid=userid;
    }

    public void changdate(List<Memo> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void OnOpen(OpenActivity openActivity){
        this.openActivity=openActivity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(this.resourceID, null);
        }
        else{
            view=convertView;
        }
        id=getItem(position).getId();
        button=(Button)view.findViewById(R.id.delete_memo_button);
        button.setOnClickListener(new BTlistener(id));


        textView1=(TextView)view.findViewById(R.id.memo_textview);
        textView1.setText(getItem(position).getContext());
        textView1.setMaxLines(2);

        return view;
    }


    private class BTlistener implements OnClickListener{
        private int id;

        public BTlistener(int id){
            this.id=id;
            Log.i("id",String.valueOf(id));
        }

        @Override
        public void onClick(View v) {
            //Log.i("memo","button");
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            popwin=layoutInflater.inflate(R.layout.popwind_memo_choose,null);
            popupWindow=new PopupWindow(popwin, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);

            textView=(TextView)popwin.findViewById(R.id.memo_null_textview);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            deleteBT=(Button)popwin.findViewById(R.id.memo_delete_button);
            deleteBT.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDB userDB=new UserDB(context);
                    //Log.i("id1",String.valueOf(id));
                    userDB.deleteonememo(id);
                    changdate(userDB.readmemo(userid));
                    popupWindow.dismiss();
                }
            });

            editBT=(Button)popwin.findViewById(R.id.memo_edit_button);
            editBT.setOnClickListener(new listener(this.id));
            //Log.i("id",String.valueOf(id));
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(popwin, 0, 0);

        }
    }



    private class listener implements OnClickListener{
        private int id1;

        public listener(int id){
            this.id1=id;
        }

        @Override
        public void onClick(View v) {
            openActivity.Openactivity(this.id1);
            popupWindow.dismiss();
            //Log.i("open", String.valueOf(this.id1));
        }
    }

}
