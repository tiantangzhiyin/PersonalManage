package com.example.personalmanage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddRemindActivity extends AppCompatActivity {
    private EditText add_remind_title;
    private EditText add_remind_type;
    private EditText add_remind_content;
    private EditText add_remind_month;
    private EditText add_remind_day;
    private EditText add_remind_hour;
    private EditText add_remind_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remind);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_add_remind);
        setSupportActionBar(toolbar);

        add_remind_title=(EditText)findViewById(R.id.add_remind_title);
        add_remind_type=(EditText)findViewById(R.id.add_remind_type);
        add_remind_content=(EditText)findViewById(R.id.add_remind_content);
        add_remind_month=(EditText)findViewById(R.id.add_remind_month);
        add_remind_day=(EditText)findViewById(R.id.add_remind_day);
        add_remind_hour=(EditText)findViewById(R.id.add_remind_hour);
        add_remind_minute=(EditText)findViewById(R.id.add_remind_minute);

        initData();
    }
    private void initData(){
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        add_remind_title.setText(title);
        add_remind_type.setText("schedule");
        add_remind_content.setText(content);
    }
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.add_remind_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            case R.id.add_remind_submit:
                submit();
                break;
        }
        return true;
    }
    //添加行程
    private void submit(){
        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String title=add_remind_title.getText().toString();
        String type=add_remind_type.getText().toString();
        String content=add_remind_content.getText().toString();
        String month=add_remind_month.getText().toString();
        String day=add_remind_day.getText().toString();
        String hour=add_remind_hour.getText().toString();
        String minute=add_remind_minute.getText().toString();
        if(title.equals("")){
            Toast.makeText(this,"标题不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(month.equals("")||day.equals("")){
            Toast.makeText(this,"日期不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(hour.equals("")||minute.equals("")){
            hour="9";
            minute="0";
        }
        ContentValues values=new ContentValues();
        values.put("title",title);
        values.put("type",type);
        values.put("content",content);
        values.put("month",month);
        values.put("day",day);
        values.put("hour",hour);
        values.put("minute",minute);
        db.insert("Remind",null,values);
        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
        finish();
    }
}
