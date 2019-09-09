package com.example.personalmanage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class PersonalDatabaseHelper extends SQLiteOpenHelper{

    private Context mContext;
    public static String CREATE_PROJECT="create table Project("
            +"id integer primary key autoincrement,"
            +"title text,"
            +"type text,"
            +"content text,"
            +"master text)";
    public static String CREATE_MONEY="create table Money("
            +"id integer primary key autoincrement,"
            +"project_id integer,"
            +"message text,"
            +"payment real,"
            +"year integer,"
            +"day integer,"
            +"month integer)";
    public static String CREATE_MEMBER="create table Member("
            +"id integer primary key autoincrement,"
            +"project_id integer,"
            +"member_name text,"
            +"rank text,"
            +"number text)";
    public static String CREATE_CULTURE="create table Culture("
            +"id integer primary key autoincrement,"
            +"image text,"
            +"title text,"
            +"content text)";
    public static String CREATE_CLASS="create table Class("
            +"class_id text,"
            +"name text,"
            +"teacher text,"
            +"room text,"
            +"start_week integer,"
            +"end_week integer)";
    public static String CREATE_CLASSTIME="create table ClassTime("
            +"class_id text,"
            +"name text,"
            +"week integer,"
            +"start_time integer,"
            +"end_time integer)";
    public static String CREATE_SCHEDULE="create table Schedule("
            +"schedule_id integer primary key autoincrement,"
            +"title text,"
            +"date text,"
            +"content text)";
    public static String CREATE_REMIND="create table Remind("
            +"id integer primary key autoincrement,"
            +"type text,"
            +"title text,"
            +"content text,"
            +"month integer,"
            +"day integer,"
            +"hour integer,"
            +"minute integer)";

    public PersonalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PROJECT);//创建项目表
        db.execSQL(CREATE_MONEY);//创建资金表
        db.execSQL(CREATE_MEMBER);//创建成员表
        db.execSQL(CREATE_CULTURE);//创建文体活动表
        db.execSQL(CREATE_CLASS);//创建课程表
        db.execSQL(CREATE_CLASSTIME);//创建课程时间表
        db.execSQL(CREATE_SCHEDULE);//创建笔记表
        db.execSQL(CREATE_REMIND);//创建提醒表
        setFirstDatabase(db);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists Project");
        db.execSQL("drop table if exists Money");
        db.execSQL("drop table if exists Member");
        db.execSQL("drop table if exists Culture");
        db.execSQL("drop table if exists Class");
        db.execSQL("drop table if exists ClassTime");
        db.execSQL("drop table if exists Schedule");
        db.execSQL("drop table if exists Remind");
        onCreate(db);
    }
    private void setFirstDatabase(SQLiteDatabase db){//初始化数据库表
        ContentValues values=new ContentValues();
        //添加项目表信息
        values.put("title","Project 1");
        values.put("master","张三");
        values.put("content","负责人：张三");
        values.put("type","国家级项目");
        db.insert("Project",null,values);
        values.clear();
        values.put("title","Project 2");
        values.put("master","李四");
        values.put("type","区级项目");
        db.insert("Project",null,values);
        values.clear();
        values.put("title","Project 3");
        values.put("master","王五");
        db.insert("Project",null,values);
        values.clear();
        //添加资金表信息
        values.put("project_id",1);
        values.put("message","启动资金");
        values.put("payment",250.00);
        values.put("year",2018);
        values.put("month",4);
        db.insert("Money",null,values);
        values.clear();
        values.put("project_id",1);
        values.put("message","支出1");
        values.put("payment",-100.00);
        values.put("year",2018);
        values.put("month",4);
        db.insert("Money",null,values);
        values.clear();
        values.put("project_id",1);
        values.put("message","支出2");
        values.put("payment",-50.00);
        values.put("year",2018);
        values.put("month",5);
        db.insert("Money",null,values);
        values.clear();
        values.put("project_id",1);
        values.put("message","支出3");
        values.put("payment",-350.00);
        values.put("year",2018);
        values.put("month",6);
        db.insert("Money",null,values);
        values.clear();
        values.put("project_id",2);
        values.put("message","启动资金");
        values.put("payment",1000.00);
        values.put("year",2018);
        values.put("month",4);
        db.insert("Money",null,values);
        values.clear();
        //添加成员表信息
        values.put("project_id",1);
        values.put("member_name","张三");
        values.put("rank","master");
        values.put("number","15878399073");
        db.insert("Member",null,values);
        values.clear();
        values.put("project_id",1);
        values.put("member_name","张强");
        values.put("rank","member");
        values.put("number","15345678910");
        db.insert("Member",null,values);
        values.clear();
        values.put("project_id",2);
        values.put("member_name","李四");
        values.put("rank","master");
        values.put("number","15777526646");
        db.insert("Member",null,values);
        values.clear();
        values.put("project_id",2);
        values.put("member_name","李鬼");
        values.put("rank","member");
        values.put("number","15003104123");
        db.insert("Member",null,values);
        values.clear();
        //添加文体活动表信息
        values.put("title","文化晚会1");
        values.put("content","4月5日晚上19点于文化广场举行。");
        db.insert("Culture",null,values);
        values.clear();
        values.put("title","文化晚会2");
        values.put("content","6月1日晚上22点于文化广场举行。");
        db.insert("Culture",null,values);
        values.clear();
        values.put("title","篮球比赛");
        values.put("content","5月4日下午16点于篮球场举行。");
        db.insert("Culture",null,values);
        values.clear();
        //添加课程表信息
        values.put("class_id","ABC00001");
        values.put("name","大学英语1");
        values.put("teacher","钱亮");
        values.put("room","11A101");
        values.put("start_week",2);
        values.put("end_week",16);
        db.insert("Class",null,values);
        values.clear();
        values.put("class_id","BDC00001");
        values.put("name","高等数学1");
        values.put("teacher","钱亮");
        values.put("room","5201");
        values.put("start_week",4);
        values.put("end_week",14);
        db.insert("Class",null,values);
        values.clear();
        //添加课程时间表信息
        values.put("class_id","ABC00001");
        values.put("name","大学英语1");
        values.put("week",2);
        values.put("start_time",1);
        values.put("end_time",2);
        db.insert("ClassTime",null,values);
        values.clear();
        values.put("class_id","ABC00001");
        values.put("name","大学英语1");
        values.put("week",4);
        values.put("start_time",5);
        values.put("end_time",6);
        db.insert("ClassTime",null,values);
        values.clear();
        values.put("class_id","BDC00001");
        values.put("name","高等数学1");
        values.put("week",1);
        values.put("start_time",3);
        values.put("end_time",4);
        db.insert("ClassTime",null,values);
        values.clear();
        values.put("class_id","BDC00001");
        values.put("name","高等数学1");
        values.put("week",3);
        values.put("start_time",3);
        values.put("end_time",4);
        db.insert("ClassTime",null,values);
        values.clear();
        //添加笔记表信息
        values.put("title","笔记1");
        values.put("date","4月1日");
        values.put("content","this is note1.");
        db.insert("Schedule",null,values);
        values.clear();
        values.put("title","笔记2");
        values.put("date","4月14日");
        values.put("content","this is note2.");
        db.insert("Schedule",null,values);
        values.clear();
        values.put("title","笔记3");
        values.put("date","4月23日");
        values.put("content","this is note3.");
        db.insert("Schedule",null,values);
        values.clear();
        values.put("title","note4");
        values.put("date","5月1日");
        values.put("content","this is note4.");
        db.insert("Schedule",null,values);
        values.clear();
        values.put("title","note5");
        values.put("date","4月18日");
        values.put("content","this is note5.");
        db.insert("Schedule",null,values);
        values.clear();
        values.put("title","note6");
        values.put("date","3月13日");
        values.put("content","this is note6.");
        db.insert("Schedule",null,values);
        values.clear();
        //添加提醒表信息
        values.put("type","schedule");
        values.put("title","note0");
        values.put("content","this is note0");
        values.put("month",1);
        values.put("day",1);
        values.put("hour",14);
        values.put("minute",30);
        db.insert("Remind",null,values);
        values.clear();
        values.put("type","schedule");
        values.put("title","测试");
        values.put("content","hello world!");
        values.put("month",5);
        values.put("day",15);
        values.put("hour",14);
        values.put("minute",30);
        db.insert("Remind",null,values);
        values.clear();
    }
}
