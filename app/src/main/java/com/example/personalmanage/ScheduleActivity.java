package com.example.personalmanage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ScheduleActivity extends AppCompatActivity {
    private int call_type;//0为新建，1为查询
    private Schedule data;
    private PersonalDatabaseHelper dbHelper;
    private EditText edit_title;
    private TextView text_date;
    private EditText edit_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);

        edit_title=(EditText)findViewById(R.id.act_sch_title_edit);
        text_date=(TextView)findViewById(R.id.act_sch_date);
        edit_content=(EditText)findViewById(R.id.act_sch_content_edit);

        initData();
    }
    private void initData(){
        dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date time = new Date(System.currentTimeMillis());
        text_date.setText("Date获取当前日期时间"+simpleDateFormat.format(time));*/

        Intent intent=getIntent();
        call_type=intent.getIntExtra("call_type",0);
        Log.d("ScheduleActivity", "call_type:"+call_type);
        if(call_type==1){//call_type为1时为选中某一记录，显示其内容
            data=(Schedule)intent.getSerializableExtra("data");
            String title=data.getTitle();
            String date=data.getGreateTime();
            String content=data.getContent();
            edit_title.setText(title);
            text_date.setText(date);
            edit_content.setText(content);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.schedule_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            case R.id.remind_schedule:
                Intent intent=new Intent(ScheduleActivity.this,AddRemindActivity.class);
                intent.putExtra("title",edit_title.getText().toString());
                intent.putExtra("content",edit_content.getText().toString());
                startActivity(intent);
                break;
            case R.id.delete_schedule:
                delete();
                break;
            case R.id.manage_schedule:
                submit();
                break;
            case R.id.export_schedule:
                export();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){//重载返回按钮功能
        setResult(RESULT_CANCELED);
        finish();
    }
    private void delete(){
        if(call_type==1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int id = data.getId();
            db.delete("Schedule", "schedule_id=?", new String[]{"" + id});
            Toast.makeText(this,"data delete",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }
    private void submit(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        /*int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int week=calendar.get(Calendar.DAY_OF_WEEK);//外国星期天为一周的第一天*/
        String date=month+"月"+day+"日";
        String title=edit_title.getText().toString();
        String content=edit_content.getText().toString();
        Log.d("updateData", "title:"+title+" date:"+date+" content:"+content);

        ContentValues values=new ContentValues();
        values.put("title",title);
        values.put("date",date);
        values.put("content",content);

        if(call_type==0){//增加数据
            db.insert("Schedule",null,values);
        }else if(call_type==1){//更新数据
            int id=data.getId();
            db.update("Schedule",values,"schedule_id=?",new String[]{""+id});
        }
        setResult(RESULT_OK);
        finish();
    }
    //导出为文件
    private void export(){
        String filename=data.getTitle();
        String content=data.getContent();
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput(filename,Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
            Toast.makeText(this,"文件已导出至/data/data/com.example.personalmanage/files/",Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
