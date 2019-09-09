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

public class ClassActivity extends AppCompatActivity {
    private int call_type;
    private Classes data;
    private PersonalDatabaseHelper dbHelper;
    private EditText edit_name;
    private EditText edit_id;
    private EditText edit_teacher;
    private EditText edit_room;
    private EditText edit_start_week;
    private EditText edit_end_week;
    private EditText edit_week_1;
    private EditText edit_week_2;
    private EditText edit_start_time_1;
    private EditText edit_start_time_2;
    private EditText edit_end_time_1;
    private EditText edit_end_time_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_class);
        setSupportActionBar(toolbar);

        edit_name=(EditText) findViewById(R.id.act_class_title);
        edit_id=(EditText) findViewById(R.id.act_class_id);
        edit_teacher=(EditText) findViewById(R.id.act_class_teacher);
        edit_room=(EditText) findViewById(R.id.act_class_room);
        edit_start_week=(EditText) findViewById(R.id.act_class_start_week);
        edit_end_week=(EditText) findViewById(R.id.act_class_end_week);
        edit_week_1=(EditText) findViewById(R.id.act_class_week_1);
        edit_week_2=(EditText) findViewById(R.id.act_class_week_2);
        edit_start_time_1=(EditText) findViewById(R.id.act_class_start_time_1);
        edit_start_time_2=(EditText) findViewById(R.id.act_class_start_time_2);
        edit_end_time_1=(EditText) findViewById(R.id.act_class_end_time_1);
        edit_end_time_2=(EditText) findViewById(R.id.act_class_end_time_2);

        initData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.class_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            case R.id.class_submit:
                submit();
                break;
            case R.id.class_delete:
                delete();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){//重载返回按钮功能
        setResult(RESULT_CANCELED);
        finish();
    }
    private void initData(){
        dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        Intent intent=getIntent();
        call_type=intent.getIntExtra("call_type",0);
        if(call_type==1){//查询或更改
            data=(Classes)intent.getSerializableExtra("data");
            edit_name.setText(data.getName());
            edit_id.setText(data.getId());
            edit_teacher.setText(data.getTeacher());
            edit_room.setText(data.getRoom());
            edit_start_week.setText(""+data.getStart_week());
            edit_end_week.setText(""+data.getEnd_week());
            int count=data.getCount();//count为课程的时间段数
            if(count==0){
                return;
            }else if(count==2){
                edit_week_2.setText(""+data.getWeek(1));
                edit_start_time_2.setText(""+data.getStart_time(1));
                edit_end_time_2.setText(""+data.getEnd_time(1));
            }
            edit_week_1.setText(""+data.getWeek(0));
            edit_start_time_1.setText(""+data.getStart_time(0));
            edit_end_time_1.setText(""+data.getEnd_time(0));
        }
    }
    //增加或更改课程信息
    private void submit(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        String name=edit_name.getText().toString();
        String class_id=edit_id.getText().toString();
        String teacher=edit_teacher.getText().toString();
        String room=edit_room.getText().toString();
        int start_week=Integer.parseInt(edit_start_week.getText().toString());
        int end_week=Integer.parseInt(edit_end_week.getText().toString());
        values.put("class_id",class_id);
        values.put("name",name);
        values.put("teacher",teacher);
        values.put("room",room);
        values.put("start_week",start_week);
        values.put("end_week",end_week);
        if(call_type==0){//添加课程
            db.insert("Class",null,values);
        }else if(call_type==1){//更新课程
            String old_id=data.getId();
            db.update("Class",values,"class_id=?",new String[]{old_id});
        }
        values.clear();
        //添加或更新具体时间
        values.put("class_id",class_id);
        values.put("name",name);
        String str_week_1=edit_week_1.getText().toString();
        if(!str_week_1.isEmpty()){
            int week_1=Integer.parseInt(str_week_1);
            int start_time_1=Integer.parseInt(edit_start_time_1.getText().toString());
            int end_time_1=Integer.parseInt(edit_end_time_1.getText().toString());
            values.put("week",week_1);
            values.put("start_time",start_time_1);
            values.put("end_time",end_time_1);
            if(call_type==0){
                db.insert("ClassTime",null,values);
            }else if(call_type==1){
                String old_id=data.getId();
                int old_week=data.getWeek(0);
                db.update("ClassTime",values,"class_id=? and week=?",new String[]{old_id,""+old_week});
            }
        }
        String str_week_2=edit_week_2.getText().toString();
        if(!str_week_2.isEmpty()){
            int week_2=Integer.parseInt(str_week_2);
            int start_time_2=Integer.parseInt(edit_start_time_2.getText().toString());
            int end_time_2=Integer.parseInt(edit_end_time_2.getText().toString());
            values.put("week",week_2);
            values.put("start_time",start_time_2);
            values.put("end_time",end_time_2);
            if(call_type==0){
                db.insert("ClassTime",null,values);
            }else if(call_type==1){
                String old_id=data.getId();
                int old_week=data.getWeek(1);
                db.update("ClassTime",values,"class_id=? and week=?",new String[]{old_id,""+old_week});
            }
        }
        setResult(RESULT_OK);
        finish();
    }
    //删除课程
    private void delete(){
        if(call_type==1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String class_id = data.getId();
            db.delete("Class", "class_id=?", new String[]{class_id});//删除课程信息
            db.delete("ClassTime", "class_id=?", new String[]{class_id});//删除课程时间信息
            Toast.makeText(this,"data delete",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }
}
//ArrayAdapter adapter= ArrayAdapter.createFromResource(getContext(),R.array.money_pay_type,android.R.layout.simple_list_item_1);
//money_type.setAdapter(adapter);
/*
money_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    //当AdapterView中的item被选中的时候执行的方法。
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str=money_type.getItemAtPosition(position).toString();

        Toast.makeText(getContext(),"you click:"+position+" "+str+" "+id,Toast.LENGTH_SHORT).show();
    }
    @Override
    //未选中时的时候执行的方法
    public void onNothingSelected(AdapterView<?> parent) {

        }
});  */
    /*spinner动态加载数据
    //适配器
    ArrayAdapter<String> adapter;
    adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
    //设置样式
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //加载适配器
    spinner.setAdapter(arr_adapter);
    */