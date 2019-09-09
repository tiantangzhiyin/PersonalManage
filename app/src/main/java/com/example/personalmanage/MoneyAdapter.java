package com.example.personalmanage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {
    private List<Money> moneyList;

    public MoneyAdapter(List<Money> moneyList){
        this.moneyList=moneyList;
    }
    @Override
    public int getItemCount(){
        return moneyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView money_item_payment;
        TextView money_item_message;
        TextView money_item_time;

        public ViewHolder(View view){
            super(view);
            money_item_payment=(TextView)view.findViewById(R.id.money_item_payment);
            money_item_message=(TextView)view.findViewById(R.id.money_item_message);
            money_item_time=(TextView)view.findViewById(R.id.money_item_time);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建ViewHolder实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//对子项数据赋值
        Money money=moneyList.get(position);
        float payment=money.getPayment();
        String message=money.getMessage();
        String time=money.getYear()+"年"+money.getMonth()+"月"+money.getDay()+"日";
        holder.money_item_payment.setText(""+payment);
        holder.money_item_message.setText(message);
        holder.money_item_time.setText(time);
    }

    public void updateData(List<Money> data) {
        this.moneyList = data;
        notifyDataSetChanged();
    }
    public void addItem(Money money) {
        if(moneyList == null) {
            moneyList = new ArrayList<>();
        }
        moneyList.add(0,money);
        notifyItemInserted(0);//更新数据集
    }
    // 由于Adapter内部是直接在首个Item位置做增加操作，增加完毕后列表移动到首个Item位置
    //mLayoutManager.scrollToPosition(0);
}
