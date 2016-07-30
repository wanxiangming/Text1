package listObject;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Team {
    private String team_name;
    private int team_id;
    private int isread;
    private Boolean isManager=false;
    private Boolean isKing=false;
    private Boolean isChecked=false;

    public Team(){

    }

    public void setTeam_name(String team_name){
        this.team_name=team_name;
    }

    public void setTeam_id(int team_id){
        this.team_id=team_id;
    }


    public void setIsChecked(Boolean isChecked){
        this.isChecked=isChecked;
    }


    public void setIsread(int isread){
        this.isread=isread;
    }


    public void setIsKing(){
        this.isKing=true;
    }

    public Boolean IsKing(){
        return this.isKing;
    }


    public String getTeam_name(){
        return this.team_name;
    }

    public int getTeam_id(){
        return this.team_id;
    }


    public Boolean getIsChecked(){
        return isChecked;
    }


    public int getIsread(){
        return this.isread;
    }
}
