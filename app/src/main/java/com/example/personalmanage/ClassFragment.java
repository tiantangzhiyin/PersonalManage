package com.example.personalmanage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ClassFragment extends Fragment {
    private PersonalDatabaseHelper dbHelper;
    private List<ClassTime> classTimeList;
    private List<Classes> classesList;
    private Button class_add;
    private RecyclerView recycler_class;
    private ClassAdapter adapter;
    private TextView class_month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.class_fragment,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        recycler_class=(RecyclerView)getActivity().findViewById(R.id.recycler_class);
        class_add=(Button)getActivity().findViewById(R.id.class_add);
        class_month=(TextView)getActivity().findViewById(R.id.class_month);

        initData();
        //showClassesList();
    }
    private void initData(){
        //设置课表当前月份
        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH)+1;
        class_month.setText(month+"月");
        //获取课表表头
        classTimeList=new ArrayList<>();
        ClassTime classTime=new ClassTime("1","2");
        classTimeList.add(classTime);
        classTime=new ClassTime("3","4");
        classTimeList.add(classTime);
        classTime=new ClassTime("5","6");
        classTimeList.add(classTime);
        classTime=new ClassTime("7","8");
        classTimeList.add(classTime);
        classTime=new ClassTime("9","10");
        classTimeList.add(classTime);
        //获取课程信息
        classesList=new ArrayList<>();
        dbHelper=new PersonalDatabaseHelper(getContext(),"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Class",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String class_id=cursor.getString(cursor.getColumnIndex("class_id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String teacher=cursor.getString(cursor.getColumnIndex("teacher"));
                String room=cursor.getString(cursor.getColumnIndex("room"));
                int start_week=cursor.getInt(cursor.getColumnIndex("start_week"));
                int end_week=cursor.getInt(cursor.getColumnIndex("end_week"));
                String text=name+"@"+room;
                //Log.d("ClassFragment", "initClass: "+text);
                Cursor cursor_time=db.query("ClassTime",null,"class_id=?",new String[]{class_id},null,null,null);
                Classes classes=new Classes(class_id,name,cursor_time.getCount());
                classes.setTeacher(teacher);
                classes.setRoom(room);
                classes.setStart_week(start_week);
                classes.setEnd_week(end_week);
                int position=0;
                if(cursor_time.moveToFirst()){
                    do{
                        int week=cursor_time.getInt(cursor_time.getColumnIndex("week"));
                        int start_time=cursor_time.getInt(cursor_time.getColumnIndex("start_time"));
                        int end_time=cursor_time.getInt(cursor_time.getColumnIndex("end_time"));
                        classes.setWeek(position,week);
                        classes.setStart_time(position,start_time);
                        classes.setEnd_time(position,end_time);
                        //添加简略信息至课表
                        setClassTimeList(class_id,text,week,start_time,end_time);
                        position++;
                    }while(cursor_time.moveToNext());
                }
                cursor_time.close();
                classesList.add(classes);
            }while (cursor.moveToNext());
        }
        cursor.close();
        //用RecyclerView实现课程列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recycler_class.setLayoutManager(layoutManager);
        adapter=new ClassAdapter(classTimeList);
        recycler_class.setAdapter(adapter);
        //注册事件监听器
        initClickListener();
    }
    private void initClickListener(){
        class_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getContext(),ClassActivity.class);
                intent.putExtra("call_type",0);
                startActivityForResult(intent,0);
            }
        });
        adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(String class_id,int week) {
                Classes find_class=null;
                for(int i=0;i<classesList.size();i++){
                    Classes classes=classesList.get(i);
                    if(classes.getId().equals(class_id)){
                        find_class=classes;
                        break;
                    }
                }
                if(find_class!=null){
                    //Toast.makeText(getContext(),class_id+":"+week,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext(),ClassActivity.class);
                    intent.putExtra("call_type",1);
                    intent.putExtra("data",find_class);
                    startActivityForResult(intent,1);
                }
                else
                    Toast.makeText(getContext(),"no class",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClassTimeList(String class_id,String text,int week,int start,int end){//text为课程简略信息,week为星期几,start为开始节数,end为结束节数
        int big_start=(start+1)/2;//开始大节
        int big_end=(end+1)/2;//结束大节
        int count=big_end-big_start+1;//大节数
        int index=big_start-1;
        //Log.d("ClassTime", "setClassList: "+index);
        while (count>0){
            ClassTime classTime=classTimeList.get(index);
            classTime.setIds(week-1,class_id);
            switch (week){
                case 1:
                    classTime.setText_Mon(text);
                    break;
                case 2:
                    classTime.setText_Tue(text);
                    break;
                case 3:
                    classTime.setText_Wed(text);
                    break;
                case 4:
                    classTime.setText_Thu(text);
                    break;
                case 5:
                    classTime.setText_Fri(text);
                    break;
                case 6:
                    classTime.setText_Sat(text);
                    break;
                case 7:
                    classTime.setText_Sun(text);
                    break;
            }
            classTimeList.set(index,classTime);
            index++;
            count--;
        }
    }
    private void showClassesList(){
        for(int i=0;i<classesList.size();i++){
            Classes classes=classesList.get(i);
            Log.d("showClassesList", classes.getName());
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            initData();
            Toast.makeText(getContext(),"class succeeded",Toast.LENGTH_SHORT).show();
        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(getContext(),"class canceled",Toast.LENGTH_SHORT).show();
        }
    }
}
