package unit;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/8/18.
 */
public class MyAnimation {
    private Activity activity;


    public MyAnimation(Activity activity){
        this.activity=activity;
    }


    public void transAnimation(View view, int startX, int endX,int startY,int endY, Animation.AnimationListener listener){
        AnimationSet animationset=new AnimationSet(true);
        TranslateAnimation translateAnimation=new TranslateAnimation(startX,endX,startY,endY);
        //AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
        //alphaAnimation.setDuration(700);
        //alphaAnimation.setFillAfter(true);
        translateAnimation.setDuration(400);
        translateAnimation.setFillAfter(true);

        animationset.addAnimation(translateAnimation);
        //animationset.addAnimation(alphaAnimation);
        animationset.setFillAfter(true);
        view.startAnimation(animationset);
        //linearLayout.startAnimation(alphaAnimation);
        translateAnimation.setAnimationListener(listener);
    }
}
