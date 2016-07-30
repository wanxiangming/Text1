package fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unite.administrator.test2.R;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Memo_Choose_Fragment extends Fragment {
    private EditText editText=null;
    private TextView textView=null;
    private Button editBT=null;
    private Button deleteBT=null;
    private FragmentManager fragmentManager=null;
    private long time;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit, container, false);


        editText=(EditText)view.findViewById(R.id.memo_edit);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, inputManager.SHOW_FORCED);

//        fragmentManager=getFragmentManager();
//
//        editText=(EditText)view.findViewById(R.id.memo_edit_edittext);
//        editText.setVisibility(View.GONE);
//        textView=(TextView)view.findViewById(R.id.memo_null_textview);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Memo_Choose_Fragment memo_choose_fragment= (Memo_Choose_Fragment) fragmentManager.findFragmentByTag("choose");
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.remove(memo_choose_fragment);
//                fragmentTransaction.commit();
//            }
//        });
//
//        deleteBT=(Button)view.findViewById(R.id.memo_delete_button);
//
//        editBT=(Button)view.findViewById(R.id.memo_edit_button);
//        editBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textView.setVisibility(View.GONE);
//                editText.setVisibility(View.VISIBLE);
//                editText.setFocusable(true);
//                editText.setFocusableInTouchMode(true);
//                editText.requestFocus();
//                editBT.setVisibility(View.GONE);
//                deleteBT.setVisibility(View.GONE);
//                InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//                inputManager.toggleSoftInput(0, inputManager.SHOW_FORCED);
//            }
//        });

        return view;
    }


}
