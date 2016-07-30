package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unite.administrator.test2.R;

import listObject.SerializableForTrans_notifyInfo;

/**
 * Created by Administrator on 2015/8/18.
 */
public class NotifyContext_Fragment extends Fragment {
    private TextView textView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notify_context,container,false);

        textView=(TextView)view.findViewById(R.id.notify_context_textview);

        Intent intent=getActivity().getIntent();
        SerializableForTrans_notifyInfo serializableForTrans_notifyInfo= (SerializableForTrans_notifyInfo) intent.getSerializableExtra("messagesinfo");

        String notify=serializableForTrans_notifyInfo.getMessages();
        String ss=notify.replace("\n", "\n\n");
        ss=serializableForTrans_notifyInfo.getMaker_name()+":"+"\n"+ss;
        textView.setText(ss);
        return  view;
    }
}
