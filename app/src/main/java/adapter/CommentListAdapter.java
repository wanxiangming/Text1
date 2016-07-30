package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unite.administrator.test2.R;

import java.util.List;

import listObject.Comment;

/**
 * Created by Administrator on 2015/8/19.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {
    private int resourceid;
    private List<Comment> comment;
    private TextView makername=null;
    private TextView commentcontext=null;

    public CommentListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
        this.comment=objects;
    }

    public void changData(List<Comment> comments){
        this.comment.clear();
        this.comment.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(this.resourceid, null);
        }
        else{
            view=convertView;
        }
        makername=(TextView)view.findViewById(R.id.comment_makername);
        commentcontext=(TextView)view.findViewById(R.id.comment_context);
        makername.setText(getItem(position).getMaker_name());
        commentcontext.setText(getItem(position).getComment());
        return view;
    }
}
