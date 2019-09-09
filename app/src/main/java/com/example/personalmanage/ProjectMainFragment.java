package com.example.personalmanage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProjectMainFragment extends Fragment {
    private int project_id;
    private EditText edit_project_title;
    private EditText edit_project_content;
    private EditText edit_project_type;
    private Button project_save;
    private ProjectActivity projectActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.project_fragment_main,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        projectActivity=(ProjectActivity)getActivity();
        edit_project_title=(EditText)projectActivity.findViewById(R.id.frag_project_title);
        edit_project_content=(EditText)projectActivity.findViewById(R.id.frag_project_content);
        edit_project_type=(EditText)projectActivity.findViewById(R.id.frag_project_type);
        project_save=(Button)projectActivity.findViewById(R.id.fragment_project_save);

        initData();
    }
    private void initData(){
        project_id=projectActivity.getProject_id();
        PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Project",null,"id=?",new String[]{""+project_id},null,null,null);
        if(cursor.moveToFirst()){//数据库中存在该项目
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            String type=cursor.getString(cursor.getColumnIndex("type"));
            edit_project_title.setText(title);
            edit_project_content.setText(content);
            edit_project_type.setText(type);
        }else{//数据库中查找不到该项目
            Toast.makeText(getContext(),"该项目不存在！",Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        if(project_id==0){
            project_save.setText("新建项目");
        }
        //为保存项目信息添加事件监听
        project_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_project();
                projectActivity.setIsChange(true);
            }
        });
    }
    //更新项目信息
    public void update_project(){
        PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //查询项目负责人
        String master=null;
        Cursor cursor=db.query("Member",null,"project_id=? and rank=?",new String[]{""+project_id,"master"},null,null,null);
        if(cursor.moveToFirst()){//存在负责人
            master=cursor.getString(cursor.getColumnIndex("member_name"));
        }else {
            master="无";
        }
        cursor.close();
        //获得新的项目信息
        ContentValues values=new ContentValues();
        String title=edit_project_title.getText().toString();
        String content=edit_project_content.getText().toString();
        String type=edit_project_type.getText().toString();
        values.put("title",title);
        values.put("content",content);
        values.put("type",type);
        values.put("master",master);
        if(project_id==0){//新建
            Cursor find=db.query("Project",null,"title=?",new String[]{title},null,null,null);
            if(find.moveToFirst()){//数据库中已存在该项目
                Toast.makeText(getContext(),"已存在该项目!",Toast.LENGTH_SHORT).show();
            }else{
                db.insert("Project",null,values);
                find=db.query("Project",null,"title=?",new String[]{title},null,null,null);
                if(find.moveToFirst()){
                    Toast.makeText(getContext(),"项目添加成功!",Toast.LENGTH_SHORT).show();
                    int id=find.getInt(cursor.getColumnIndex("id"));
                    project_id=id;
                    projectActivity.setProject_id(id);
                }
                project_save.setText("提交修改");
            }
            find.close();
        }else{//更新
            db.update("Project",values,"id=?",new String[]{""+project_id});
            Toast.makeText(getContext(),"项目信息已更新!",Toast.LENGTH_SHORT).show();
        }

    }
}
