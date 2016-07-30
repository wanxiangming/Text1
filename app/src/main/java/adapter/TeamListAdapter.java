package adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unite.administrator.test2.R;

import java.util.List;

import listObject.Team;

/**
 * Created by Administrator on 2015/8/10.
 */
public class TeamListAdapter extends ArrayAdapter<Team> {
    private int textViewId;
    public List<Team> teamlist;

    public TeamListAdapter(Context context, int resource, List<Team> objects) {
        super(context, resource, objects);
        this.textViewId=resource;
        this.teamlist = objects;
    }


    @Override
    public int getCount() {
        //Log.i("num",String.valueOf(teamlist.size()));
        return teamlist.size();
    }


    public void changeData(List<Team> newlist){
        this.teamlist.clear();
        this.teamlist.addAll(newlist);
        notifyDataSetChanged();
    }

    @Override
    public Team getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            //layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=LayoutInflater.from(getContext()).inflate(this.textViewId, null);    //设置每一个item的样子
        }
        else{
            view=convertView;
        }
        Team team =getItem(position);
        TextView textView=(TextView)view.findViewById(R.id.teamlist_textview_item);
        textView.setText("(" + String.valueOf(team.getTeam_id()) + ") " + team.getTeam_name());
        ImageView imageView=(ImageView)view.findViewById(R.id.teamlist_imageview_item);
        if(team.getIsread() != 0){
            imageView.setImageResource(R.drawable.list_view_tip);
        }else {
            imageView.setImageDrawable(null);
        }
        return view;
    }
}
