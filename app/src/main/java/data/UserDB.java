package data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import data.DatabasesHelper;
import listObject.Memo;
import listObject.Messages;
import listObject.Team;

/**
 * Created by Administrator on 2015/8/8.
 * UserDB使用结束后，一定要调用closeAll()方法，释放数据库连接
 */
public class UserDB {
    private DatabasesHelper databasesHelper;
    private LocalOnlionUserInfo localOnlionUserInfo;
    private SQLiteDatabase db;
    private SQLiteDatabase dbread;
    private Context context = null;


    public UserDB(Context context) {
        this.context = context;
        databasesHelper = DatabasesHelper.getInstance(context);
        db = databasesHelper.getWritableDatabase();
    }


    public Context getContext() {
        return this.context;
    }


    /**
     * 向本地添加用户信息
     *
     * @param accon
     * @param password
     * @param id
     */
    public void adduser(String accon, String password, int id, String username) {
        Log.i("Userdb", accon + "   " + password + "   " + id);
        db.execSQL("INSERT INTO users_info(accon,password,userid,username) VALUES(?,?,?,?)", new String[]{accon, password, String.valueOf(id), username});
    }


    /**
     * 找到本机用户状态为在线的那个账号,将它的信息设置到LocalOnlionUserInfo类中
     */
    public LocalOnlionUserInfo findOnlionUser() {
        //Cursor cursor=db.rawQuery("SELECT * FROM users_info WHERE accon="+"'"+accon+"'"+" and password="+"'"+password+"'"+"isonlion=1",null);
        Cursor cursor = db.rawQuery("SELECT * FROM users_info WHERE isonlion=1", null);
        localOnlionUserInfo = new LocalOnlionUserInfo();
        if (cursor.moveToFirst()) {
            localOnlionUserInfo.setIsOnlion(1);
            localOnlionUserInfo.setAccon(cursor.getString(1));
            localOnlionUserInfo.setPassword(cursor.getString(2));
            localOnlionUserInfo.setId(cursor.getInt(4));
            localOnlionUserInfo.setUsername(cursor.getString(5));
        } else {
            localOnlionUserInfo.setIsOnlion(0);
        }
        cursor.close();
        return localOnlionUserInfo;
    }


    /**
     * 粗略的查找，看看本地有没有匹配这个账号的用户存在,存在返回真，不存在返回假
     */
    public boolean findUser(String accon) {
        Cursor cursor = db.rawQuery("SELECT * FROM users_info WHERE accon='" + accon + "'", null);
        if (cursor.moveToFirst()) {
            cursor.close();
            //Log.i("Userdb", "true");
            return true;
        } else {
            cursor.close();
            //Log.i("Userdb", "false");
            return false;
        }
    }


    /**
     * 设置该用户的在线状态
     *
     * @param stat
     */
    public void onlionstat(int userid, int stat) {
        db.execSQL("UPDATE users_info SET isonlion=0");
        db.execSQL("UPDATE users_info SET isonlion=? WHERE userid=?", new String[]{String.valueOf(stat), String.valueOf(userid)});
    }


    /**
     * 通过账号找到数据，更新用户密码
     */
    public void updatauserinfo(String accon, String passworod, String username) {
        db.execSQL("UPDATE users_info SET password=?,username=? WHERE accon=?", new String[]{passworod, username, accon});
    }



    /**
     * 删除本地，指定userid的not_info的nofity信息
     */
    public void deleteNot_infoNotify(int userid) {
        db.execSQL("DELETE FROM not_info WHERE flag='notify' and userid=" + "'" + userid + "'");
    }


    /**
     * 向本地的not_info表中插入数据
     * 这个函数会先将与要插入的信息内容一样的记录删除，再插入,这样，由于isread字段默认是1，所以只要是从
     * 服务器getNewInfo读过来的数据，一定可以在本地插入的时候，isread字段保持为1
     */
    public void addNot_info(int userid, int team_id, String team_name,long time, String messages, String flag, int maker_id, String maker_name, int messagesid) {
        long operatetime = System.currentTimeMillis();
        //Log.i("time",String.valueOf(operatetime));
        db.execSQL("DELETE FROM not_info WHERE userid=? and team_id=? and flag=? and maker_id=? and messagesid=?", new String[]{
                String.valueOf(userid), String.valueOf(team_id), flag, String.valueOf(maker_id), String.valueOf(messagesid)
        });
        db.execSQL("INSERT INTO not_info(userid,team_id,team_name,time,messages,flag,maker_id,maker_name,operatetime,messagesid) VALUES (?,?,?,?,?,?,?,?,?,?)", new String[]{
                String.valueOf(userid), String.valueOf(team_id),team_name, String.valueOf(time), messages, flag,
                String.valueOf(maker_id), maker_name, String.valueOf(operatetime), String.valueOf(messagesid)
        });
    }


    public void addAllNot_info(int userid, int team_id, long time, String messages, String flag, int maker_id, String maker_name, int messagesid, String team_name) {
        long operatetime = System.currentTimeMillis();
        db.execSQL("INSERT INTO not_info(userid,team_id,time,messages,flag,maker_id,maker_name,operatetime,messagesid,isread,team_name) VALUES(?,?,?,?,?,?,?,?,?,?,?" +
                ")", new String[]{String.valueOf(userid), String.valueOf(team_id),
                String.valueOf(time), messages, flag, String.valueOf(maker_id), maker_name,
                String.valueOf(operatetime), String.valueOf(messagesid), "0", team_name});
    }


    /**
     * 删除本地not_info表中的一条数据
     */
    public void deleteOneJoinRequest(int userid, int team_id, int maker_id) {
        db.execSQL("DELETE FROM not_info WHERE userid=" + "'" + userid + "' and " +
                "team_id='" + team_id + "' and " +
                "maker_id='" + maker_id + "' and " +
                "flag='join_request'");
    }


    /**
     * 以team_id为分组，查找出本地not_info的信息
     * 这个方法仅仅供给主界面显示消息列表使用
     * team_id  team_name  time     isreadsum
     * 10030    你好      1231415     4
     */
    public List<Team> getLimitTeamList(int userid) {
        String sql = "SELECT team_id,team_name,MAX(operatetime) AS time,SUM(isread) AS num " +
                "FROM not_info " +
                "WHERE userid=" + "'" + userid + "' " +
                "GROUP BY team_id " +
                "ORDER BY time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Team> list = new ArrayList<Team>();
        while (cursor.moveToNext()) {
            Team team = new Team();
            team.setTeam_id(cursor.getInt(0));
            team.setTeam_name(cursor.getString(1));
            team.setIsread(cursor.getInt(3));
            list.add(team);            //Log.i("cursor", String.valueOf(cursor.getInt(3)));
        }
        cursor.close();
        return list;
    }


    /**
     * 将指定团队ID的isread标签清0,同时要满足是该用户的信息的要求
     *
     * @param team_id
     */
    public void changeTheTeamIsReadInfoOnNotify(int team_id, int userid, int messagesid) {
        String sql = "UPDATE not_info SET isread=0 WHERE team_id=" + "'" + team_id + "' and userid=" + "'" + userid + "' and messagesid=" + "'" + messagesid + "'";
        db.execSQL(sql);
    }

    public void changeTheTeamIsReadInfoOnRequest(int team_id, int userid) {
        String sql = "UPDATE not_info SET isread=0 WHERE team_id=" + "'" + team_id + "' and userid=" + "'" + userid + "' and flag='join_request'";
        db.execSQL(sql);
    }


    /**
     * 查找出该用户在本地的not_info表的所有信息，插入到一个messages对象泛型的list中
     */
    public List<Messages> getMessagesList(int team_id, int userid) {
        String sql = "SELECT * FROM not_info WHERE team_id=" + "'" + team_id + "' AND userid='" + userid + "' ORDER BY time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Messages> messagelist = new ArrayList<>();
        while (cursor.moveToNext()) {
            Messages m = new Messages();
            m.setUserid(cursor.getInt(1));
            m.setTeam_id(cursor.getInt(2));
            m.setTeam_name(cursor.getString(3));
            m.setTime(cursor.getLong(4));
            m.setMessages(cursor.getString(5));
            m.setFlag(cursor.getString(6));
            m.setMaker_id(cursor.getInt(7));
            m.setMaker_name(cursor.getString(8));
            m.setIsread(cursor.getInt(10));
            m.setNotifyid(cursor.getInt(11));
            messagelist.add(m);
        }
        cursor.close();
        return messagelist;
    }


    /**
     * 用来存入一条备忘
     */
    public void addmemo(int userid, String memo) {
        long time = System.currentTimeMillis();
        db.execSQL("INSERT INTO memo(userid,memo,time) VALUES(?,?,?)", new String[]{
                String.valueOf(userid), memo, String.valueOf(time)
        });
    }


    /**
     * 用来读取备忘
     */
    public List<Memo> readmemo(int userid) {
        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE userid='" + userid + "' ORDER BY time DESC", null);
        List<Memo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Memo memo = new Memo();
            memo.setContext(cursor.getString(2));
            memo.setId(cursor.getInt(0));
            list.add(memo);
        }
        cursor.close();
        return list;
    }

    /**
     * 读取一条备忘
     */
    public String readmemoOne(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE id='" + id + "'", null);
        cursor.moveToFirst();
        String s = cursor.getString(2);
        return s;
    }

    /**
     * 更新一条备忘
     */
    public void updateonememo(String memo, int id) {
        db.execSQL("UPDATE memo SET memo=? WHERE id=?", new String[]{
                memo,
                String.valueOf(id)
        });
    }

    /**
     * 用来删除一条备忘
     */
    public void deleteonememo(int id) {
        Log.i("userdb", String.valueOf(id));
        db.execSQL("DELETE FROM memo WHERE id=?", new String[]{String.valueOf(id)});
    }

    /**
     * 修改本地用户的用户名
     */
    public void changeName(String name, int userid) {
        db.execSQL("UPDATE users_info SET username=? WHERE userid=?", new String[]{name, String.valueOf(userid)});
    }


    /**
     * 清楚该用户的该team的所有信息
     */
    public void clearTeamInfo(int userid, int team_id) {
        db.execSQL("DELETE FROM not_info WHERE userid=? and team_id=?", new String[]{String.valueOf(userid), String.valueOf(team_id)});
        //db.execSQL("DELETE FROM team_info WHERE userid=? and member_of_team=?",new String[]{String.valueOf(userid),String.valueOf(team_id)});
    }


    public void closeAll() {
        db.close();
        databasesHelper.close();
    }














    /**
     * 查出该用户所加入的所有team的teamname和teamid，插入到一个Team泛型的List中
     */
    public List<Team> getTeaminfo(int userid) {
        String sql = "SELECT member_of_team,team_name FROM team_info WHERE userid=" + userid;
        Cursor cursor = db.rawQuery(sql, null);
        List<Team> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Team team = new Team();
            team.setTeam_name(cursor.getString(1));
            team.setTeam_id(cursor.getInt(0));
            list.add(team);
            //Log.i("UserDB", "cursor");
        }
        cursor.close();
        return list;
    }

    /**
     * 查看该用户是否已经加入了该团队，加入了返回true，没加入返回false
     */
    public Boolean isMemberOfThis(int team_id, int userid) {
        String sql = "SELECT member_of_team FROM team_info WHERE member_of_team=" + "'" + team_id + "' and " +
                "userid='" + userid + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * 查一下该用户在该team中是否拥有king权限
     */
    public Boolean checkPermissionOfTeam(int userid, int team_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM team_info WHERE userid=?,member_of_team=?", new String[]{String.valueOf(userid), String.valueOf(team_id)});
        cursor.moveToNext();
        if (cursor.getInt(4) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更改team_info表的指定userid，指定member_of_team，的mamnager_of_team字段
     */
    public void updateManager_of_team(int userid, int member_of_team) {
        db.execSQL("UPDATE team_info SET manager_of_team='1' WHERE userid=" + "'" + userid + "' AND member_of_team='" + member_of_team + "'");
    }


    /**
     * 更改team_info表的指定userid，指定member_of_team，的king_of_team字段
     */
    public void updateKing_of_team(int userid, int member_of_team) {
        db.execSQL("UPDATE team_info SET king_of_team=1 WHERE userid=" + "'" + userid + "' and member_of_team='" + member_of_team + "'");
    }

    /**
     * 删除本地，指定userid的team_info
     */
    public void deleteTeamInfo(int userid) {
        db.execSQL("DELETE FROM team_info WHERE userid=" + "'" + userid + "'");
    }

    /**
     * 向本地数据库的team_info表插入数据
     */
    public void addMember_of_team(int userid, int member_of_team, String team_name) {
        db.execSQL("INSERT INTO team_info(userid,member_of_team,team_name) VALUES(?,?,?)", new String[]{String.valueOf(userid), String.valueOf(member_of_team), team_name});
    }

}
