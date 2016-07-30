package data;

/**
 * Created by Administrator on 2015/8/26.
 */
public class Request {
    private int userid;
    private String password;
    private String accon;
    private String username;
    private String flag;
    private int Cid;
    private String deviceId;
    private int team_id;
    private String messages;

    public Request() {

    }

    public void setMessages(String messages){
        this.messages=messages;
    }

    public void setTeam_id(int team_id){
        this.team_id=team_id;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccon(String accon) {
        this.accon = accon;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


}
