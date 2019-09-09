package com.example.personalmanage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{
    private List<ClassTime> mClassTimeList;
    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    public ClassAdapter(List<ClassTime> classTimeList){
        mClassTimeList=classTimeList;
    }
    @Override
    public int getItemCount(){
        return mClassTimeList.size();
    }

    //自定义一个回调接口来实现Click事件
    public interface OnItemClickListener{
        void onItemClick(String class_id,int week);
    }
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView class1;
        TextView class2;
        Button button_Mon;
        Button button_Tue;
        Button button_Wed;
        Button button_Thu;
        Button button_Fri;
        Button button_Sat;
        Button button_Sun;

        public ViewHolder(View view){
            super(view);
            class1=(TextView)view.findViewById(R.id.class1);
            class2=(TextView)view.findViewById(R.id.class2);
            button_Mon=(Button)view.findViewById(R.id.button_Mon);
            button_Tue=(Button)view.findViewById(R.id.button_Tue);
            button_Wed=(Button)view.findViewById(R.id.button_Wed);
            button_Thu=(Button)view.findViewById(R.id.button_Thu);
            button_Fri=(Button)view.findViewById(R.id.button_Fri);
            button_Sat=(Button)view.findViewById(R.id.button_Sat);
            button_Sun=(Button)view.findViewById(R.id.button_Sun);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){//创建ViewHolder实例
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){//对子项数据赋值
        final ClassTime classTime=mClassTimeList.get(position);
        holder.class1.setText(classTime.getClass1());
        holder.class2.setText(classTime.getClass2());
        holder.button_Mon.setText(classTime.getText_Mon());
        holder.button_Tue.setText(classTime.getText_Tue());
        holder.button_Wed.setText(classTime.getText_Wed());
        holder.button_Thu.setText(classTime.getText_Thu());
        holder.button_Fri.setText(classTime.getText_Fri());
        holder.button_Sat.setText(classTime.getText_Sat());
        holder.button_Sun.setText(classTime.getText_Sun());
        // 为ItemView添加点击事件
        holder.button_Mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(0),1);
            }
        });
        holder.button_Tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(1),2);
            }
        });
        holder.button_Wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(2),3);
            }
        });
        holder.button_Thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(3),4);
            }
        });
        holder.button_Fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(4),5);
            }
        });
        holder.button_Sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(5),6);
            }
        });
        holder.button_Sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(classTime.getIds(6),7);
            }
        });
    }
    /*
    private View.OnClickListener ocl=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(mOnItemClickListener!=null){
                switch (id){
                    case R.id.button_Mon:
                        mOnItemClickListener.onItemClick(1);
                        break;
                    case R.id.button_Tue:
                        mOnItemClickListener.onItemClick(2);
                        break;
                    case R.id.button_Wed:
                        mOnItemClickListener.onItemClick(3);
                        break;
                    case R.id.button_Thu:
                        mOnItemClickListener.onItemClick(4);
                        break;
                    case R.id.button_Fri:
                        mOnItemClickListener.onItemClick(5);
                        break;
                    case R.id.button_Sat:
                        mOnItemClickListener.onItemClick(6);
                        break;
                    case R.id.button_Sun:
                        mOnItemClickListener.onItemClick(7);
                        break;
                }
            }
        }
    };*/
}
