package unit;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/8/6.
 * 提供各种URL
 */
public class GetUrl {
    String accon="";
    String password="";
    String url="";


    public GetUrl() {

    }


    /**
     * 给它一对用户名和密码，它将会反回给你一个可用于登陆的URL
     * @param accon
     * @param password
     * @return
     */
    public String getloninURL(){
        this.url=URL("Unite","Unite");
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 给它一对用户名和密码，它将会返回给你一个可用于注册的URL
     * @param accon
     * @param password
     * @return
     */
    public String getregisterURL(String accon,String password,String username){
        this.url=URL("register","register");
        this.url=this.url+"accon="+accon+"&"+"password="+password+"&"+"username="+encoder(username);
        Log.i("URL", this.url);
        return this.url;
    }


    /**
    创建团队使用的URL
     */
    public String getcreateURL(String teamname,int userid){
        this.url=URL("createteam","create");
        this.url=this.url+"teamname="+encoder(teamname)+"&userid="+userid;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 从服务器获取该用户所加入的所有team的名字和ID的URL
     */
    public String get_team_name(int userid){
        this.url=URL("getinfo","getTeam_name");
        this.url=this.url+"userid="+userid;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 从服务器获取test2表 member_of_team 字段数据的URL
     * @param controller
     * @param action
     * @return
     */
    public String get_member_of_team(int userid){
        this.url=URL("getinfo","get_test2_member_of_team");
        this.url=this.url+"userid="+userid;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 从服务器获取test2表所有team信息的URL
     * @param controller
     * @param action
     * @return
     */
    public String get_team_info_from_test2(int userid){
        this.url=URL("getinfo","get_team_info_with_userid");
        this.url=this.url+"userid="+userid;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 告诉服务器我请求加入某个团队的URL
     * @param controller
     * @param action
     * @return
     */
    public String request_for_join(int userid,int team_id){
        this.url=URL("RequestForJoin","RequestForJoin");
        this.url=this.url+"userid="+userid+"&team_id="+team_id;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 与服务器轮询的URL
     * @param controller
     * @param action
     * @return
     */
    public String polling(int userid,String deviceId){
        this.url=URL("Polling","Polling");
        this.url=this.url+"userid="+userid+"&"+"deviceId="+deviceId;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 从服务器获取所有新消息的URL
     * @param controller
     * @param action
     * @return
     */
    public String getNewInfo(int userid){
        this.url=URL("getinfo","getnewinfo");
        this.url=this.url+"userid="+userid;
        Log.i("URL", this.url);
        return this.url;
    }


    /**
     * 同意加入申请的URL
     * @param controller
     * @param action
     * @return
     */
    public String agreeJoinRequest(int team_id,int maker_id,String flag){
        this.url=URL("RequestForJoin","AgreeForJoin");
        this.url=this.url+"team_id="+team_id+"&"+"maker_id="+maker_id+"&"+"flag="+flag;
        Log.i("URL",this.url);
        return this.url;
    }

    /**
     * 发送notify的URL
     * @param userid
     * @param team_id
     * @param messages
     * @return
     */
    public String sendNotify(){
        this.url=URL("Unite","Unite");
        //this.url=this.url+"userid="+userid+"&"+"team_id="+team_id+"&"+"notify="+encoder(messages);
        Log.i("URL",this.url);
        return this.url;
    }

    /**
     * 从服务器获取通告评论的URL
     * @param team_id
     * @param notifyid
     * @return
     */
    public String getNotifyComment(int Cid){
        this.url=URL("GetNotifyComment","GetNotifyComment");
        //this.url=this.url+"team_id="+team_id+"&"+"notifyid="+notifyid;
        this.url=this.url+"Cid="+Cid;
        Log.i("URL",this.url);
        return this.url;
    }


    /**
     * 发送通告评论的URL
     */
    public String sendNotifyComment(int maker_id,String comment,int notifyid){
        this.url=URL("SendNotifyComment","SendNotifyComment");
        this.url=this.url+"maker_id="+maker_id+"&"+"comment="+encoder(comment)+"&"+"Cid="+notifyid;
        Log.i("URL",this.url);
        return this.url;
    }


    /**
     * 从服务器获取所有该用户信息的URL    最重要的URL之一
     * @param controller
     * @param action
     * @return
     */
    public String getAll(int userid){
        this.url=URL("Getinfo","GetAllInfo");
        this.url=this.url+"userid="+userid;
        Log.i("URL",this.url);
        return this.url;
    }


    /**
     * 修改用户名的URL
     * @param controller
     * @param action
     * @return
     */
    public String changeName(int userid,String username){
        this.url=URL("Refactor","Rename");
        this.url=this.url+"userid="+userid+"&"+"name="+encoder(username);
        Log.i("URL",this.url);
        return this.url;
    }


    /**
     * 退出某个团队的URL
     * @param controller
     * @param action
     * @return
     */
    public String removeTeam(int userid,int team_id){
        this.url=URL("Refactor","RemoveTeam");
        this.url=this.url+"userid="+userid+"&"+"team_id="+team_id;
        Log.i("URL",this.url);
        return this.url;
    }


    /**
     * 获取某团队中所有成员的URL
     * @param controller
     * @param action
     * @return
     */
    public String getMemberList(int team_id){
        this.url=URL("Getinfo","getMember_list");
        this.url=this.url+"team_id="+team_id;
        Log.i("URL",this.url);
        return this.url;
    }



    /*
    私有方法，将URL中的 控制器 和 方法 两个参数传给它，它能够放回一个加入了控制器和方法的URL
     */
    private String URL(String controller,String action){
        return "http://182.92.70.93/unite/index.php?r="+controller+"/"+action+"&";
    }


    /*
    编码器
    对传入的字符串进行utf8编码
     */
    public String encoder(String string){
        String callback="";
        try {
            callback=URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback="";
        }
        return callback;
    }

}
