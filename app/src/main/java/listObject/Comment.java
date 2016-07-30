package listObject;

import android.content.Context;

/**
 * Created by Administrator on 2015/8/19.
 */
public class Comment {
    private int maker_id;
    private String time;
    private String comment;
    private String maker_name;

    public Comment(){

    }

    public void setMaker_id(int maker_id){
        this.maker_id=maker_id;
    }

    public void setTime(String time){
        this.time=time;
    }

    public void setComment(String comment){
        this.comment=comment;
    }

    public void setMaker_name(String maker_name){
        this.maker_name=maker_name;
    }
    public int getMaker_id(){
        return this.maker_id;
    }

    public String getTime(){
        return this.time;
    }

    public String getComment(){
        return this.comment;
    }

    public String getMaker_name(){
        return this.maker_name;
    }



}
