package com.example.personalmanage;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ViewHolder> {
    private List<Remind> remindList;
    private Context context;

    public RemindAdapter(Context context,List<Remind> remindList){
        this.remindList=remindList;
        this.context=context;
    }
    @Override
    public int getItemCount(){
        return remindList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView remind_type;
        TextView remind_time;
        TextView remind_title;

        public ViewHolder(View view){
            super(view);
            remind_type=(ImageView)view.findViewById(R.id.remind_item_type);
            remind_time=(TextView)view.findViewById(R.id.remind_item_time);
            remind_title=(TextView)view.findViewById(R.id.remind_item_title);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建ViewHolder实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//对子项数据赋值
        Remind remind=remindList.get(position);
        final String title=remind.getTitle();
        final String content=remind.getContent();
        String type=remind.getType();
        int hour=remind.getHour();
        int minute=remind.getMinute();
        //Log.d("RemindAdapter", "title:"+title+" content:"+content+" type:"+type);
        holder.remind_time.setText(hour+":"+minute);
        holder.remind_title.setText(title);
        switch (type){
            case "project":
                holder.remind_type.setImageResource(R.drawable.ic_project);
                break;
            case "culture":
                holder.remind_type.setImageResource(R.drawable.ic_culture);
                break;
            case "class":
                holder.remind_type.setImageResource(R.drawable.ic_class);
                break;
            case "schedule":
                holder.remind_type.setImageResource(R.drawable.ic_schedule);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext());
                dialog.setTitle(title);
                dialog.setMessage(content);
                dialog.show();
            }
        });
    }

    public void updateData(List<Remind> data) {
        this.remindList = data;
        notifyDataSetChanged();
    }
    public void addItem(Remind remind) {
        if(remind == null) {
            remindList = new ArrayList<>();
        }
        remindList.add(remind);
        notifyItemInserted(remindList.size()-1);//更新数据集
    }
    // 由于Adapter内部是直接在首个Item位置做增加操作，增加完毕后列表移动到首个Item位置
    //mLayoutManager.scrollToPosition(0);
    public void deleteItem(int id) {
        if(remindList == null || remindList.isEmpty()) {
            return;
        }
        int i;
        for(i=0;i<remindList.size();i++){
            Remind remind=remindList.get(i);
            if(id==remind.getId()){
                break;
            }
        }
        if(i<remindList.size()){//若能找到成员
            remindList.remove(i);
            notifyItemRemoved(i);
        }else{//找不到成员
            Toast.makeText(context,"不存在该成员！",Toast.LENGTH_SHORT).show();
        }
    }
}
