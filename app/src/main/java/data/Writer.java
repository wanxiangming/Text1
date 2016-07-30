package data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
//// TODO: 2015/8/21 writer对数据的处理，当没有数据时，报空指针，你需要对数据进行是否为空的判断
/**
 * Created by Administrator on 2015/8/20.
 * 用来对服务器传过来的
 */
public class Writer {
    private Context context;
    private UserDB userDB;
    private int userid;

    public Writer(UserDB userDB,int userid){
        this.userDB=userDB;
        this.context=userDB.getContext();
        this.userid=userid;
    }


    /**
     * 如果你给这个方法传入的数据不是空的，那么它将吧该userid的所有本地not_info信息删除，使用新传过来的这一组数据，全部
     * 重新录入一遍
     * @param list
     */
    public void getAll(List<JsonDeal> list){
        if(list.size()==0){

        }else{
            userDB.deleteNot_infoNotify(userid);
            for(int i=0;i<list.size();i++){
                userDB.addAllNot_info(userid,
                        list.get(i).getTeam_id(),
                        list.get(i).getTime(),
                        list.get(i).getMessages(),
                        list.get(i).getFlag(),
                        list.get(i).getMaker_id(),
                        list.get(i).getMaker_name(),
                        list.get(i).getMessagesId(),
                        list.get(i).getTeam_name());
            }
        }
    }


    /**
     * 它会根据服务器传过来的信息，将新信息插入到not_info表中
     * @param list
     */
    public void getNewMessages(List<JsonDeal> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFlag().equals("join_request")) {
                userDB.deleteOneJoinRequest(userid, list.get(i).getTeam_id(), list.get(i).getMaker_id());
                userDB.addNot_info(userid,
                        list.get(i).getTeam_id(),
                        list.get(i).getTeam_name(),
                        System.currentTimeMillis(),
                        list.get(i).getMessages(),
                        list.get(i).getFlag(),
                        list.get(i).getMaker_id(),
                        list.get(i).getMaker_name(),
                        0);
            } else if(list.get(i).getFlag().equals("notify")){
                userDB.addNot_info(userid,
                        list.get(i).getTeam_id(),
                        list.get(i).getTeam_name(),
                        list.get(i).getTime(),
                        list.get(i).getMessages(),
                        list.get(i).getFlag(),
                        list.get(i).getMaker_id(),
                        list.get(i).getMaker_name(),
                        list.get(i).getMessagesId());
            }
        }
    }
}
