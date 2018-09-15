package com.example.lab9map;
import utils.Constant;
import utils.DbManger;
import utils.MySQLiteHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static utils.Constant.TABLE_NAME;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener{

    MySQLiteHelper helper;
    Button verifyButton,backButton;
    EditText userText,numText;
    Button enterButton,signButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        helper = DbManger.getIntance(this);
        userText=(EditText)findViewById(R.id.user);
        numText=(EditText)findViewById(R.id.num);

        enterButton=(Button)findViewById(R.id.enter);
        signButton=(Button)findViewById(R.id.sign);
        enterButton.setOnClickListener(this);
        signButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter:
                String userString=userText.getText().toString();
                String numString=numText.getText().toString();
                if(userString.equals("")||numString.equals("")){
                    new AlertDialog.Builder(Main3Activity.this).setTitle("错误")
                            .setMessage("帐号或密码不能空").setPositiveButton("确定", null)
                            .show();//弹出消息框
                }else if(login(userString,numString)){
                    Toast.makeText(Main3Activity.this, "登陆成功!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Main3Activity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign:
                Intent intent2 = new Intent(Main3Activity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public boolean login(String username,String password){
        SQLiteDatabase db=helper.getReadableDatabase();
        String sql="select * from user where 用户名=? and 密码=?";
        Cursor cursor=db.rawQuery(sql, new String[]{username,password});
        if(cursor.moveToFirst()==true){
            Intent intent = new Intent(Main3Activity.this, MainActivity2.class);
            startActivity(intent);
            cursor.close();
            return true;
        }
        return false;
    }
    //登录用
}
