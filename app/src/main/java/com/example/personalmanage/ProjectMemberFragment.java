package com.example.personalmanage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProjectMemberFragment extends Fragment {
    private Button but_add_member;
    private RecyclerView recyclerView;
    private ProjectActivity projectActivity;
    private List<Member> memberList;
    private EditText edit_member_rank;
    private EditText edit_member_name;
    private EditText edit_member_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.project_fragment_member,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        projectActivity=(ProjectActivity)getActivity();
        but_add_member=(Button)projectActivity.findViewById(R.id.but_add_member);
        recyclerView=(RecyclerView)projectActivity.findViewById(R.id.recycler_member);
        edit_member_rank=(EditText)projectActivity.findViewById(R.id.edit_member_rank);
        edit_member_name=(EditText)projectActivity.findViewById(R.id.edit_member_name);
        edit_member_number=(EditText)projectActivity.findViewById(R.id.edit_member_number);

        initData();
    }
    private void initData(){
        memberList=new ArrayList<>();
        //查询数据库
        int project_id=projectActivity.getProject_id();
        PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Member",null,"project_id=?",new String[]{""+project_id},null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String rank=cursor.getString(cursor.getColumnIndex("rank"));
                String name=cursor.getString(cursor.getColumnIndex("member_name"));
                String number=cursor.getString(cursor.getColumnIndex("number"));
                Member member=new Member(id,project_id,name);
                member.setRank(rank);
                member.setNumber(number);
                memberList.add(member);
            }while (cursor.moveToNext());
        }
        cursor.close();

        //实现列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final MemberAdapter adapter=new MemberAdapter(getContext(),memberList);
        recyclerView.setAdapter(adapter);
        //添加成员
        but_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int project_id=projectActivity.getProject_id();
                if(project_id==0){
                    Toast.makeText(getContext(),"先添加项目!",Toast.LENGTH_SHORT).show();
                    return;
                }
                String rank=edit_member_rank.getText().toString();
                String name=edit_member_name.getText().toString();
                String number=edit_member_number.getText().toString();
                if(name.equals("")){
                    Toast.makeText(getContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //添加成员信息至成员表
                    PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("project_id",project_id);
                    values.put("rank",rank);
                    values.put("member_name",name);
                    values.put("number",number);
                    db.insert("Member",null,values);
                    values.clear();
                    //更新成员列表
                    Cursor cursor=db.query("Member",null,"project_id=? and member_name=?",new String[]{""+project_id,name},null,null,null);
                    if(cursor.moveToFirst()){
                        int id=cursor.getInt(cursor.getColumnIndex("id"));
                        Member member=new Member(id,project_id,name);
                        member.setRank(rank);
                        member.setNumber(number);
                        adapter.addItem(member);
                        Toast.makeText(getContext(),"添加成功!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"添加失败!",Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
                edit_member_rank.setText("");
                edit_member_name.setText("");
                edit_member_number.setText("");
            }
        });

    }
}