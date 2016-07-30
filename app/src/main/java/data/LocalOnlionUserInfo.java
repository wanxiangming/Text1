package data;

import android.content.Context;
import android.database.Cursor;

import data.UserDB;

/**
 * Created by Administrator on 2015/8/9.
 * 这个类用来存储本地为在线状态的用户的信息
 * 并且提供了一个public的方法isexsit()来供外界调用，这个方法会根据查询结果，返回Boolean值
 * 并且这个类的各个变量可以用来提供查询结果
 */
public class LocalOnlionUserInfo {
    private String accon;
    private String password;
    private String username;
    private int id;
    private int isonlion=0;

    public LocalOnlionUserInfo(){

    }


/**
 *这个方法会提供boolean型的放回值，本地存在已登陆用户，返回true
 */
    public Boolean isexsit(){
        if(this.isonlion==1){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 返回本地处于登陆状态的用户账号
     * @return
     */
    public String getAccon(){
        return accon;
    }


    /**
     *  返回本地处于登陆状态的用户密码
     * @return
     */
    public String getPassword(){
        return password;
    }


    /**
     * 返回本地处于登陆状态的用户ID
     * @return
     */
    public int getId(){
        return id;
    }


    public void setPassword(String password){
        this.password=password;
    }


    public void setAccon(String accon){
        this.accon=accon;
    }


    public void setId(int id){
        this.id=id;
    }


    public void setIsOnlion(int isOnlion){
        this.isonlion=isOnlion;
    }


    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return this.username;
    }


    /*
    私有方法，用来取出用户的相关信息，赋值给成员变量
     */
//    private void getlocalinfo(){
//        UserDB db=new UserDB(context);
//        Cursor c=db.findOnlionUser();
//        if(c.moveToFirst()){
//            isonlion=1;
//            accon=c.getString(1);
//            password=c.getString(2);
//            id=c.getInt(4);
//        }else{
//            isonlion=0;
//        }
//        c.close();
//    }
}
