package ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by Administrator on 2015/8/20.
 */
public class CommentEditText extends EditText {
    public CommentEditText(Context context) {
        super(context);
    }

    public CommentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return true;
    }
}
