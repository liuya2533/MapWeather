package utils;

/**
 * Created by 雷神 on 2017/11/28.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by siqi on 2017/11/23.
 * 主要是对数据库操作的工具类
 */

public class DbManger {
    private static utils.MySQLiteHelper helper;
    public static MySQLiteHelper getIntance(Context context){
        if(helper==null){
            helper=new MySQLiteHelper(context);
        }
        return helper;
    }

    /*
     *根据sql语句在数据库中执行语句
     * 数据库对象 db
     * sql语句 sql
     */
    public static void exexSQL(SQLiteDatabase db,String sql){
        if(db!=null){
            if(sql!=null && !"".equals(sql)){
                db.execSQL(sql);
            }
        }
    }
}