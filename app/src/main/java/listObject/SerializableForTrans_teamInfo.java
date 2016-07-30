package listObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/16.
 */
public class SerializableForTrans_teamInfo implements Serializable {
    private int team_id;
    private String team_name;
    private Boolean ischeck;

    public SerializableForTrans_teamInfo(){

    }

    public void setTeam_id(int team_id){
        this.team_id=team_id;
    }


    public void setTeam_name(String team_name){
        this.team_name=team_name;
    }


    public void setIscheck(Boolean ischeck){
        this.ischeck=ischeck;
    }


    public Boolean getIschecke(){
        return this.ischeck;
    }


    public int getTeam_id(){
        return this.team_id;
    }


    public String getTeam_name(){
        return this.team_name;
    }

}
