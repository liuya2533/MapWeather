package com.example.lab9map;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;
import utils.DbManger;
import utils.MySQLiteHelper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private MySQLiteHelper helper;
    RadioGroup gendergroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    String genderText;

    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    String hobby;
    List<CheckBox> checkBoxList=new ArrayList<CheckBox>();

    Spinner yearSpinner, monthSpinner, dateSpinner;
    Button submitButton;
    String[] year, month;
    int i = 0, j = 0, k = 0;
    EditText emailText;
    ArrayAdapter<CharSequence> yearAdapter, monthAdapter, dateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = DbManger.getIntance(this);//获得数据库帮助类的对象
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        //dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        emailText = (EditText) findViewById(R.id.emailText);
        year = getResources().getStringArray(R.array.year);
        month = getResources().getStringArray(R.array.month);
        yearAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, year);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(this);
        monthAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, month);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(this);

        gendergroup=(RadioGroup)findViewById(R.id.gender);
        radioButton1=(RadioButton)findViewById(R.id.man);
        radioButton2=(RadioButton)findViewById(R.id.woman);
        gendergroup.setOnCheckedChangeListener(ChangRadio);
        checkBox1=(CheckBox)findViewById(R.id.football);
        checkBox2=(CheckBox)findViewById(R.id.game);
        checkBox3=(CheckBox)findViewById(R.id.tv);
        checkBox4=(CheckBox)findViewById(R.id.reading);
        checkBoxList.add(checkBox1);
        checkBoxList.add(checkBox2);
        checkBoxList.add(checkBox3);
        checkBoxList.add(checkBox4);
        /*String[] date={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17",
                        "18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        dateAdapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,date);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(this);*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    private RadioGroup.OnCheckedChangeListener ChangRadio=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(radioButton1.getId()==checkedId){
                genderText=radioButton1.getText().toString();
            }else if(radioButton2.getId()==checkedId){
                genderText=radioButton2.getText().toString();
            }
        }
    };
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() == R.id.yearSpinner)
            if (position != i) {
                i = position;
            } else if (view.getId() == R.id.monthSpinner) {
                if (position != k) {
                    //editMonth.setText((int)parent.getItemIdAtPosition(position));
                    k = position;
                }
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        StringBuffer sb=new StringBuffer();
        for(CheckBox checkbox:checkBoxList){
            if(checkbox.isChecked()){
                sb.append(checkbox.getText().toString()+" ");
            }
        }
        hobby=sb.toString();

        EditText editText = (EditText) findViewById(R.id.firstText);
        String s = editText.getText().toString();
        EditText editText1 = (EditText) findViewById(R.id.lastText);
        String s1 = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.emailText);
        String s2 = editText2.getText().toString();
        EditText editText3 = (EditText) findViewById(R.id.passwordText);
        String s3 = editText3.getText().toString();
        String s4 = (String) yearSpinner.getSelectedItem();
        String s5 = (String) monthSpinner.getSelectedItem();
        EditText editText4=(EditText)findViewById(R.id.accountText);
        String s6=editText4.getText().toString();
        if (TextUtils.isEmpty(s3)) {
            Toast.makeText(MainActivity.this, "请输入密码!", Toast.LENGTH_LONG).show();
            return;
        } else {
            switch (v.getId()) {
                case R.id.submitButton:
                /*
        getReadableDatabase() getWritableDatabase() 创建或打开数据库
        如果数据库不存在则打开数据库，如果数据库存在直接打开数据库
        默认情况下两个数据库都表示打开或者创建可读可写的数据库对象，如果磁盘已满或者是数据库本身权限等情况下
        getReadableDatabase()打开的是只读数据库
         */
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Constant.FIRSTNAME, s);
                    values.put(Constant.LASTNAME, s1);
                    values.put(Constant.EMAIL, s2);
                    values.put(Constant.YEAR, s4);
                    values.put(Constant.MONTH, s5);
                    values.put(Constant.GENDER,genderText);
                    values.put(Constant.HOBBY,hobby);
                    values.put(Constant.ACCOUNT,s6);
                    values.put(Constant.PASSWORD,s3);
                    long result = db.insert(Constant.TABLE_NAME, null, values);
                    if (result > 0) {
                        Toast.makeText(MainActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "注册失败!", Toast.LENGTH_LONG).show();
                    }
                    db.close();
            }
            Intent intent = new Intent(MainActivity.this, Main3Activity.class);
            //intent.putExtra("key",emailText.getText().toString());

            Bundle bundle = new Bundle();
            bundle.putString("key", emailText.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

}
