package data;

import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/8.
 */
public class DatabasesHelper extends SQLiteOpenHelper {
    private static final int versionnumber =1;
    private static final String localdatabase="M";

    public DatabasesHelper(Context context) {
        super(context,localdatabase,null,versionnumber);
    }


    private static DatabasesHelper mInstance;

    public synchronized static DatabasesHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabasesHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users_info(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "accon VARCHAR(17)," +
                "password VARCHAR(33)," +
                "isonlion INTEGER(1) DEFAULT 0," +
                "userid INTEGER," +
                "username VARCHAR(16))" );


        /*
        只有member_of_team字段存的是真正的teamid，下面两个字段存0或1，标示该用户是或者不是该team的manager或king
        这个表实行，全部删除，全部重新添加的方式来更新，数据由服务器端提供
         */
//        db.execSQL("CREATE TABLE IF NOT EXISTS team_info(id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "userid INTEGER," +
//                "member_of_team INTEGER," +
//                "manager_of_team INTEGER DEFAULT 0," +
//                "king_of_team INTEGER DEFAULT 0," +
//                "team_name VARCHAR(16))");

        /*
        这个表用来装所有的消息，messages用来装具体的消息，flag作为消息的标志位，告诉系统这条消息属于什么消息，time字段用来
        存储这条消息到达服务器的时间,operatetime是本机接收这条消息的时间
         */
        //// TODO: 2015/8/15 team_name 这个字段暂时用不着,在需要的地方，比如main_Activity中，团队名被用联合查询的方式找出来了
        db.execSQL("CREATE TABLE IF NOT EXISTS not_info(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userid INTEGER(11)," +
                "team_id INTEGER(11)," +
                "team_name VARCHAR(16)," +
                "time INTEGER(20),"+
                "messages TEXT," +
                "flag INTEGER(2)," +
                "maker_id INTEGER(11)," +
                "maker_name VARCHAR(16)," +
                "operatetime LONG," +
                "isread INTEGER(1) DEFAULT 1," +
                "messagesid INTEGER)");

        /*
        这个表用来存备忘
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS memo(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userid INTEGER(11)," +
                "memo TEXT," +
                "time LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        //db.execSQL("ALTER TABLE user ADD COLUMN other TEXT");

    }
}
