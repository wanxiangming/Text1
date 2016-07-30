package listObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/18.
 */
public class SerializableForTrans_notifyInfo implements Serializable{
    private int team_id;
    private String time;
    private String messages;
    private String maker_name;
    private int notifyId;
    private int userid;

    public SerializableForTrans_notifyInfo(){

    }

    public void setTeam_id(int team_id){
        this.team_id=team_id;
    }

    public void setTime(String time){
        this.time=time;
    }

    public void setNotifyId(int notifyId){
        this.notifyId=notifyId;
    }

    public void setMessages(String messages){
        this.messages=messages;
    }

    public void setMaker_name(String maker_name){
        this.maker_name=maker_name;
    }

    public int getTeam_id(){
        return this.team_id;
    }

    public String getTime(){
        return this.time;
    }

    public String getMessages(){
        return this.messages;
    }

    public String getMaker_name(){
        return this.maker_name;
    }

    public int getNotifyId(){
       return this.notifyId;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public int getUserid(){
        return userid;
    }
}
