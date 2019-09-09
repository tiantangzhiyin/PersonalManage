package com.example.personalmanage;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> mScheduleList;
    private OnItemClickListener onItemClickListener;// 事件回调监听

    public ScheduleAdapter(List<Schedule> scheduleList){
        mScheduleList=scheduleList;
    }
    // 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(Schedule schedule);//参数可以被外部使用
        //void onItemClick(View view,int position);
    }
    // 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @Override
    public int getItemCount(){
        return mScheduleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView schedule_title;
        TextView schedule_time;

        public ViewHolder(View view){
            super(view);
            schedule_title=(TextView)view.findViewById(R.id.schedule_item_title);
            schedule_time=(TextView)view.findViewById(R.id.schedule_item_time);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建ViewHolder实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {//对子项数据赋值
        final Schedule schedule=mScheduleList.get(position);
        holder.schedule_title.setText(schedule.getTitle());
        holder.schedule_time.setText(schedule.getGreateTime());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(onItemClickListener != null) {
                    //int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(schedule);
                }
            }
        });
    }
    /*
    public void updateData(List<Schedule> data) {
        this.mScheduleList = data;
        notifyDataSetChanged();
    }
    public void addItem(Schedule schedule) {
        if(mScheduleList == null) {
            mScheduleList = new ArrayList<>();
        }
        mScheduleList.add(0, schedule);
        notifyItemInserted(0);//更新数据集
    }
        // 由于Adapter内部是直接在首个Item位置做增加操作，增加完毕后列表移动到首个Item位置
        //mLayoutManager.scrollToPosition(0);
    public void deleteItem() {
        if(mScheduleList == null || mScheduleList.isEmpty()) {
            return;
        }
        mScheduleList.remove(0);
        notifyItemRemoved(0);
    }*/
}
