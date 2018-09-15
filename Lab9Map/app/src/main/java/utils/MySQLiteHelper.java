package utils;

/**
 * Created by 雷神 on 2017/11/28.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by siqi on 2017/11/23.
 *
 * SQLiteQpenHelper
 * 1.提供了onCreate() onUpgrade()等创建数据库更新数据库的方法
 * 2.提供了获取数据库对象的函数
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    /*
     * context 上下文对象
     * name 表示创建数据库的名称
     * factory 游标工厂
     * version 表示创建数据库的版本 要求>=1
     */
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteHelper(Context context){
        super(context,Constant.DATABASE_NAME,null,Constant.DATABASE_VERSION);
    }
    /*
     * 当创建数据库时回调的函数
     * db是数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("tag","------onCreate------");
        String sql="create table "+Constant.TABLE_NAME+"("+Constant.FIRSTNAME+" varchar(20),"+Constant.LASTNAME+" varchar(20),"+Constant.EMAIL+" varchar(20),"+Constant.YEAR+" varchar(20),"+Constant.MONTH+" varchar(20),"+Constant.GENDER+" varchar(20),"+Constant.HOBBY+" varchar(20),"+Constant.ACCOUNT+" varchar(20),"+Constant.PASSWORD+" varchar(20))";//创建表
        db.execSQL(sql);//执行sql语句
    }

    /*
     * 当数据库版本更新时回调的函数
     * db是数据库对象
     * oldVersion是数据库旧版本
     * newVersion是数据库新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("tag","------onCreate------");
    }

    /*
     * 当数据库打开时回调的函数
     * db是数据库对象
     */
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        Log.i("tag","------onCreate------");
    }
}
