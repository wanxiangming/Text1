package adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.unite.administrator.test2.R;

import org.w3c.dom.Text;

import java.util.List;

import listObject.Team;

/**
 * Created by Administrator on 2015/8/16.
 */
public class CheckTeamListAdapter extends ArrayAdapter<Team>{
    private List<Team> list=null;
    private Context context=null;
    private int resourceid;

    public CheckTeamListAdapter(Context context, int resource, List<Team> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resourceid=resource;
        this.list=objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
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
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.sendcheck_checkbox);
        TextView textView=(TextView)view.findViewById(R.id.sendcheck_textview);

        textView.setText("("+String.valueOf(getItem(position).getTeam_id())+")"+getItem(position).getTeam_name());
        checkBox.setOnCheckedChangeListener(new checkboxlistener(position));

        return  view;
    }


    private class checkboxlistener implements CompoundButton.OnCheckedChangeListener{
        private int position;

        public checkboxlistener(int position){
            this.position=position;
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItem(position).setIsChecked(isChecked);
        }
    }


    public List<Team> getList(){
        return list;
    }
}
