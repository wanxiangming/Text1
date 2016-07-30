package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unite.administrator.test2.R;

import java.util.List;

import listObject.Member;


/**
 * Created by Administrator on 2015/8/25.
 */
public class MemberListAdapter extends ArrayAdapter<Member> {
    private Context context;
    private int resourceId;
    private TextView textView=null;

    public MemberListAdapter(Context context, int resource, List<Member> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(this.resourceId, null);
        }
        else{
            view=convertView;
        }
        textView=(TextView)view.findViewById(R.id.memberlist_textview);
        textView.setText(getItem(position).getMemberName());

        return view;
    }
}
