package com.example.personalmanage;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Fragment[] fragments;
    private DrawerLayout mDrawerLayout;
    private TextView now_time;
    private List<Remind> remindList;
    private RecyclerView recycler_remind;
    private RemindAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dashboard_black_24dp);//修改返回按钮图标
        }

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        now_time=(TextView)findViewById(R.id.remind_nowtime);
        recycler_remind=(RecyclerView)findViewById(R.id.recycler_remind);

        initFragment();
        initData();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    //实例化各个碎片
    private void initFragment(){
        Fragment fragment1 = new ProjectFragment();
        Fragment fragment2 = new CultureFragment();
        Fragment fragment3 = new ClassFragment();
        Fragment fragment4 = new ScheduleFragment();
        fragments = new Fragment[]{fragment1, fragment2, fragment3,fragment4};
        replaceFragment(0);
    }
    //替换第i个碎片到指定容器
    private void replaceFragment(int i){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout,fragments[i]);
        transaction.commit();
    }
    //初始化数据
    private void initData(){
        //获取当前天数
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        now_time.setText(year+"年"+month+"月"+day+"日");
        //初始化今日行程列表
        remindList=new ArrayList<>();
        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Remind",null,"month=? and day=?",new String[]{""+month,""+day},null,null,"hour");
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String type=cursor.getString(cursor.getColumnIndex("type"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                int hour=cursor.getInt(cursor.getColumnIndex("hour"));
                int minute=cursor.getInt(cursor.getColumnIndex("minute"));
                Remind remind=new Remind();
                remind.setId(id);
                remind.setTitle(title);
                remind.setType(type);
                remind.setContent(content);
                remind.setMonth(month);
                remind.setDay(day);
                remind.setHour(hour);
                remind.setMinute(minute);
                addRemindList(remind);
            }while (cursor.moveToNext());
        }
        cursor.close();
        addRemindList_class();
        //实现列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_remind.setLayoutManager(layoutManager);
        adapter=new RemindAdapter(this,remindList);
        recycler_remind.setAdapter(adapter);
    }
    //添加行程列表信息
    private void addRemindList(Remind remind){
        if(remind!=null){
            int i;
            for(i=0;i<remindList.size();i++){
                Remind temp=remindList.get(i);
                if(remind.getHour()<temp.getHour()){
                    //若待添加的行程的小时比当前的行程小，则插入当前行程前
                    break;
                }else if(remind.getHour()==temp.getHour()){
                    //若待添加的行程的小时与当前的行程相等，则比较分钟
                    if(remind.getMinute()<temp.getMinute())
                        break;
                }
            }
            remindList.add(i,remind);//按时间顺序加入队列
        }
    }
    //把课程安排添加至行程列表
    private void addRemindList_class(){
        //获取当前星期几
        Calendar calendar = Calendar.getInstance();
        int weeks = calendar.get(Calendar.DAY_OF_WEEK);
        //外国一周的第一天是星期天,转换为本国计数
        if(weeks==1){
            weeks=7;
        }else {
            weeks=weeks-1;
        }
        //若设置添加课程提醒，则添加到当天行程（默认添加）
        SharedPreferences pref;
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isAddRemindClass=pref.getBoolean("isAddRemindClass",true);
        if (isAddRemindClass){
            PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor cursor=db.query("Class",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String class_id=cursor.getString(cursor.getColumnIndex("class_id"));
                    String name=cursor.getString(cursor.getColumnIndex("name"));
                    String room=cursor.getString(cursor.getColumnIndex("room"));
                    //int start_week=cursor.getInt(cursor.getColumnIndex("start_week"));
                    //int end_week=cursor.getInt(cursor.getColumnIndex("end_week"));
                    Cursor cursor_time=db.query("ClassTime",null,"class_id=? and week=?",new String[]{class_id,""+weeks},null,null,"start_time");
                    if(cursor_time.moveToFirst()){
                        do{
                            int start_time=cursor_time.getInt(cursor_time.getColumnIndex("start_time"));
                            int end_time=cursor_time.getInt(cursor_time.getColumnIndex("end_time"));
                            //根据获取信息构建提醒实例
                            Remind remind=new Remind();
                            remind.setType("class");
                            remind.setTitle(name);
                            remind.setContent("教室："+room+"\n时间："+start_time+"--"+end_time+"小节");
                            //把课程节数转换为具体时间并添加到行程表
                            switch (start_time){
                                case 1:
                                    remind.setHour(8);
                                    remind.setMinute(30);
                                    break;
                                case 3:
                                    remind.setHour(10);
                                    remind.setMinute(30);
                                    break;
                                case 5:
                                    remind.setHour(14);
                                    remind.setMinute(30);
                                    break;
                                case 7:
                                    remind.setHour(16);
                                    remind.setMinute(30);
                                    break;
                                case 9:
                                    remind.setHour(19);
                                    remind.setMinute(30);
                                    break;
                            }
                            addRemindList(remind);
                        }while(cursor_time.moveToNext());
                    }
                    cursor_time.close();
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }
    //实现底部导航事件监听
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_project:
                    replaceFragment(0);
                    return true;
                case R.id.navigation_culture:
                    replaceFragment(1);
                    return true;
                case R.id.navigation_class:
                    replaceFragment(2);
                    return true;
                case R.id.navigation_schedule:
                    replaceFragment(3);
                    return true;
            }
            return false;
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.main_menu,menu);
        //getMenuInflater().inflate(R.menu.class_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            case android.R.id.home://修改原来的返回键的作用
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_setting:
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this,"default",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public void onBackPressed(){//重载返回按钮功能
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("退出程序");
        dialog.setMessage("是否退出程序？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"取消操作",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        initData();
    }
}
