package com.example.personalmanage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment {
    private List<Schedule> scheduleList;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.schedule_fragment,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView=(SearchView)getActivity().findViewById(R.id.search_schedule);
        recyclerView=(RecyclerView)getActivity().findViewById(R.id.recycler_schedule);
        fab=(FloatingActionButton)getActivity().findViewById(R.id.fab_schedule);

        initData();
    }
    private void initData(){
        //初始化笔记列表
        scheduleList=new ArrayList<>();
        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(getContext(),"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Schedule",null,null,null,null,null,"date");
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("schedule_id"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String date=cursor.getString(cursor.getColumnIndex("date"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                Schedule sch=new Schedule(id,title,date);
                sch.setContent(content);
                scheduleList.add(sch);
            }while (cursor.moveToNext());
        }
        cursor.close();
        //实现列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(adapter);
        //注册事件监听器
        initClickListener();
    }
    private void initClickListener(){
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Schedule schedule) {
                Intent intent=new Intent(getContext(),ScheduleActivity.class);
                intent.putExtra("call_type",1);
                intent.putExtra("data",schedule);
                startActivityForResult(intent,1);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // 当点击搜索按钮时触发该方法
                queryText(query);
                searchView.clearFocus(); //清除焦点，收软键盘
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { // 当搜索内容改变时触发该方法
                if (TextUtils.isEmpty(newText)){//若搜索内容为空则不进行筛选
                    recyclerView.setAdapter(adapter);
                }
                else {
                    queryText(newText);
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ScheduleActivity.class);
                intent.putExtra("call_type",0);
                startActivityForResult(intent,0);
            }
        });
    }
    private void queryText(String query){//搜索笔记
        List<Schedule> queryList=new ArrayList<>();
        for(int i=0;i<scheduleList.size();i++){
            Schedule schedule=scheduleList.get(i);
            String title=schedule.getTitle();
            //Log.d("Schdule", "queryText: "+"query text:"+query+" scheduleList"+i+" title:"+title);
            if(title.startsWith(query))//若title以query为开头
                queryList.add(schedule);
            else
                Log.d("Schedule", title);
        }
        ScheduleAdapter adp=new ScheduleAdapter(queryList);
        recyclerView.setAdapter(adp);
        adp.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Schedule schedule) {
                Intent intent=new Intent(getContext(),ScheduleActivity.class);
                intent.putExtra("call_type",1);
                intent.putExtra("data",schedule);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            initData();
            Toast.makeText(getContext(),"schedule succeeded",Toast.LENGTH_SHORT).show();
        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(getContext(),"schedule canceled",Toast.LENGTH_SHORT).show();
        }
    }
}
