package listObject;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Messages {
    private int team_id;    //接受消息的那个团队ID
    private int userid;
    private String team_name;
    private Long time;
    private String messages;
    private String flag;
    private int maker_id;
    private String maker_name;
    private int isread;
    private int notifyid;


    public Messages(){

    }


    public void setTeam_id(int team_id){
        this.team_id=team_id;
    }


    public void setUserid(int userid){
        this.userid=userid;
    }


    public void setTeam_name(String team_name){
        this.team_name=team_name;
    }


    public void setTime(Long time){
        this.time=time;
    }


    public void setMessages(String messages){
        this.messages=messages;
    }


    public void setFlag(String flag){
        this.flag=flag;
    }


    public void setMaker_id(int maker_id){
        this.maker_id=maker_id;
    }


    public void setMaker_name(String maker_name){
        this.maker_name=maker_name;
    }

    public void setNotifyid(int notifyid){
        this.notifyid=notifyid;
    }


    public void setIsread(int isread){
        this.isread=isread;
    }


    public int getTeam_id(){
        return this.team_id;
    }


    public int getUserid(){
        return this.userid;
    }


    public int getMaker_id(){
        return this.maker_id;
    }


    public int getIsread(){
        return this.isread;
    }


    public String getTeam_name(){
        return this.team_name;
    }


    public String getMessages(){
        return this.messages;
    }


    public String getFlag(){
        return this.flag;
    }


    public String getMaker_name(){
        return this.maker_name;
    }


    public String getTime(){
        String time=String.valueOf(this.time);
        String newtime=time+"000";
        long new_time=Long.valueOf(newtime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 hh时mm分");

        return simpleDateFormat.format(new_time);
    }


    public int getnotifyId() {
        return this.notifyid;
    }
}
