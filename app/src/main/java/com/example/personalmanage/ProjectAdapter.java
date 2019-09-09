package com.example.personalmanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectAdapter extends ArrayAdapter<Project> {
    private int resourceId;

    public ProjectAdapter(Context context,int textViewResourceId,List<Project> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){//getView()方法在每个子项滚动到屏幕内时被调用
        Project project=getItem(position);//获取当前项的实例
        View view;
        Viewholder viewholder;

        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewholder=new Viewholder();
            viewholder.project_name=(TextView) view.findViewById(R.id.project_name);
            viewholder.master_name=(TextView) view.findViewById(R.id.master_name);
            viewholder.project_type=(TextView)view.findViewById(R.id.project_type);
            view.setTag(viewholder);//将Viewholder存储在View中
        }else{
            view=convertView;
            viewholder=(Viewholder) view.getTag();//重新获取Viewholder
        }
        viewholder.project_name.setText(project.getProject_name());
        viewholder.master_name.setText("负责人："+project.getMaster_name());
        viewholder.project_type.setText(project.getProject_type());
        return view;
    }
    class Viewholder{
        TextView project_name;
        TextView master_name;
        TextView project_type;
    }
}
