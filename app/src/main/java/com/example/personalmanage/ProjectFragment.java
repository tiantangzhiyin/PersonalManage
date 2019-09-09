package com.example.personalmanage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProjectFragment extends Fragment {
    private View view;
    private List<Project> projectList;
    private ProjectAdapter adapter;
    private ListView listView;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.project_fragment,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //MainActivity activity=(MainActivity) getActivity();//可通过getActivity()获得当前碎片相关联的活动实例
        listView=(ListView)view.findViewById(R.id.list_project);
        fab= (FloatingActionButton)view.findViewById(R.id.fab);

        initData();
    }
    private void initData(){//初始化项目列表
        projectList=new ArrayList<>();
        adapter=new ProjectAdapter(view.getContext(),R.layout.project_item,projectList);

        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(getContext(),"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Project",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String master=cursor.getString(cursor.getColumnIndex("master"));
                String type=cursor.getString(cursor.getColumnIndex("type"));
                Project pro=new Project(title,master);
                pro.setProject_id(id);
                pro.setProject_type(type);
                projectList.add(pro);
            }while (cursor.moveToNext());
        }
        cursor.close();

        listView.setAdapter(adapter);
        initClickListener();
    }
    private void initClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project=projectList.get(position);
                //Toast.makeText(getContext(),position+project.getProject_name(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),ProjectActivity.class);
                intent.putExtra("project_id",project.getProject_id());
                startActivityForResult(intent,1);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ProjectActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            initData();
            Toast.makeText(getContext(),"project succeeded",Toast.LENGTH_SHORT).show();
        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(getContext(),"project canceled",Toast.LENGTH_SHORT).show();
        }

    }
}
