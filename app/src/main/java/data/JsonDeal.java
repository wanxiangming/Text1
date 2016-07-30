package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/9.
 * jsonObject各个get方法中传入的字符串，标注的是从服务器端传过来的json数据的key名
 * 同时这个key名我们约定它和mysql数据表中的字段名一致
 * 只要服务器发过来的是关于mysql表的jsonObject数据，你就可以通过这个类中的get方法，取出数据
 * 注意，传入的数据一定要是一个Object
 */
public class JsonDeal {
    private String read;
    private JSONObject jsonObject=null;
    private JSONArray jsonArray=null;
    //private List<Integer> teamlist;

//test1表中的对应字段
    private int id;
    private String username;
    private String accon;
    private String password;
    private int phonenum;
    private int isonlion;
    private int userid;
    private int isLogin;
    private int isregister;


//team表中的对应字段
    private int team_id;
    private String team_name;
    private String member_list;
    private String notification;


//各种标志位
    private String messages;
    private String flag;
    private String maker_name;
    private int maker_id;
    private long time;
    private int messagesid;
    private String comment;

    private List<Integer> member_of_team;
    private List<Integer> king_of_team;
    private List<String> team_name_list;



    /**
     * 该构造函数需要传入一个从服务器发过来的json格式的字符串流
     * @param string
     */
    public JsonDeal(){

    }


    public String getMessages(){
        return messages;
    }

    public int getIsRegister(){
        return isregister;
    }



    public String getMaker_name(){
        return maker_name;
    }


    public int getMaker_id(){
        return maker_id;
    }


    public int getId(){
        return id;
    }


    public String getUsername(){
        return username;
    }


    public String getAccon(){
        return accon;
    }


    public String getPassword(){
        return password;
    }


    public int getPhonenum(){
        return phonenum;
    }


    public int getIsonlion(){
        return isonlion;
    }


    /**
     * 将发过来的 "key":["aaa","bbb","cc",]这种类型的字符串数组，解析到一个list中
     */
    public List<String> get_list_of_team_name(){
        return team_name_list;
    }


    /**
     *将发过来的 "key":["123","321","222"]这种类型的json数据，解析到一个list中
     * @return
     */
    public List<Integer> get_list_member_of_team(){
        return member_of_team;
    }
    public List<Integer> get_list_king_of_team(){
        return king_of_team;
    }


    public int getUserid(){
        return userid;
    }


    /**
     * 该函数返回的是team表中的id字段，是团队ID
     * @return
     */
    public int getTeam_id(){
        return team_id;
    }


    /**
     * team表中的team_name字段
     * @return
     */
    public String getTeam_name(){
        return team_name;
    }


    public String getFlag(){
        return flag;
    }


    public Long getTime(){
        return time;
    }

    public int getMessagesId() {
        return  messagesid;
    }

    public String getComment(){
        return  comment;
    }

    public int getIsLogin(){
        return this.isLogin;
    }
}
