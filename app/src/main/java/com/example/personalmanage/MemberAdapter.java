package com.example.personalmanage;



import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{
    private List<Member> mMemberList;
    private Context context;

    public MemberAdapter(Context context,List<Member> memberList){
        this.context=context;
        mMemberList=memberList;
    }
    @Override
    public int getItemCount(){
        return mMemberList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView member_rank;
        TextView member_name;
        TextView member_number;
        Button member_call;
        Button member_delete;

        public ViewHolder(View view){
            super(view);
            member_rank=(TextView)view.findViewById(R.id.member_rank);
            member_name=(TextView)view.findViewById(R.id.member_name);
            member_number=(TextView)view.findViewById(R.id.member_number);
            member_call=(Button) view.findViewById(R.id.member_call);
            member_delete=(Button) view.findViewById(R.id.member_delete);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建ViewHolder实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//对子项数据赋值
        final Member member=mMemberList.get(position);
        holder.member_rank.setText(member.getRank());
        holder.member_name.setText(member.getName());
        holder.member_number.setText(member.getNumber());

        holder.member_call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String number=member.getNumber();
                if((number!=null)&&(!number.equals(""))){
                    //使用隐式Intent拨打电话
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+number));
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context,"没有联系电话!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.member_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int id=member.getId();
                PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(context,"PersonalManage.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Member","id=?",new String[]{""+id});
                deleteItem(id);
            }
        });
    }

    public void updateData(List<Member> data) {
        this.mMemberList = data;
        notifyDataSetChanged();
    }
    public void addItem(Member member) {
        if(mMemberList == null) {
            mMemberList = new ArrayList<>();
        }
        mMemberList.add(member);
        notifyItemInserted(mMemberList.size()-1);//更新数据集
    }
    // 由于Adapter内部是直接在首个Item位置做增加操作，增加完毕后列表移动到首个Item位置
    //mLayoutManager.scrollToPosition(0);
    public void deleteItem(int id) {
        if(mMemberList == null || mMemberList.isEmpty()) {
            return;
        }
        int i;
        for(i=0;i<mMemberList.size();i++){
            Member member=mMemberList.get(i);
            if(id==member.getId()){
                break;
            }
        }
        if(i<mMemberList.size()){//若能找到成员
            mMemberList.remove(i);
            notifyItemRemoved(i);
        }else{//找不到成员
            Toast.makeText(context,"不存在该成员！",Toast.LENGTH_SHORT).show();
        }
    }

}
