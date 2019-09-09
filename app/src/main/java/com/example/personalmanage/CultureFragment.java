package com.example.personalmanage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CultureFragment extends Fragment {
    private Button but_add;
    private ListView listView;
    private CultureAdapter adapter;
    private List<Culture> cultureList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.culture_fragment,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        but_add=(Button)getActivity().findViewById(R.id.but_add_culture);
        listView=(ListView)getActivity().findViewById(R.id.list_culture);

        initData();
    }
    private void initData(){
        cultureList=new ArrayList<>();
        adapter=new CultureAdapter(getContext(),R.layout.culture_item,cultureList);

        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(getContext(),"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Culture",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String image=cursor.getString(cursor.getColumnIndex("image"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                Culture cul=new Culture(id,title);
                cul.setImage(image);
                cul.setContent(content);
                cultureList.add(cul);
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
                Culture culture=cultureList.get(position);
                //Toast.makeText(getContext(),"list"+position+":"+culture.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),CultureActivity.class);
                intent.putExtra("call_type",1);
                intent.putExtra("data",culture);
                startActivityForResult(intent,1);
            }
        });
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),CultureActivity.class);
                intent.putExtra("call_type",0);
                startActivityForResult(intent,0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            initData();
            Toast.makeText(getContext(),"culture succeeded",Toast.LENGTH_SHORT).show();
        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(getContext(),"culture canceled",Toast.LENGTH_SHORT).show();
        }
    }
}
