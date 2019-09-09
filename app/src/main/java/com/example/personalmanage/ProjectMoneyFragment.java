package com.example.personalmanage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectMoneyFragment extends Fragment {
    private int project_id;
    private ProjectActivity projectActivity;
    private RecyclerView recycler_money;
    private List<Money> moneyList;
    private MoneyAdapter adapter;
    private TextView money_all_pay;
    private TextView money_surplus;
    private EditText money_payment;
    private EditText money_message;
    private Button money_add;
    private float all_pay;
    private float surplus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.project_fragment_money,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        projectActivity=(ProjectActivity)getActivity();
        recycler_money=(RecyclerView)projectActivity.findViewById(R.id.recycler_money);
        money_all_pay=(TextView)projectActivity.findViewById(R.id.money_all_pay);
        money_surplus=(TextView)projectActivity.findViewById(R.id.money_surplus);
        money_payment=(EditText)projectActivity.findViewById(R.id.money_payment);
        money_message=(EditText)projectActivity.findViewById(R.id.money_message);
        money_add=(Button)projectActivity.findViewById(R.id.money_add);

        initData();

    }

    private void initData(){
        moneyList=new ArrayList<>();
        project_id=projectActivity.getProject_id();
        all_pay=0;
        surplus=0;
        //查询数据库
        PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Money",null,"project_id=?",new String[]{""+project_id},null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                float payment=cursor.getFloat(cursor.getColumnIndex("payment"));
                String message=cursor.getString(cursor.getColumnIndex("message"));
                int year=cursor.getInt(cursor.getColumnIndex("year"));
                int month=cursor.getInt(cursor.getColumnIndex("month"));
                int day=cursor.getInt(cursor.getColumnIndex("day"));
                //Log.d("TableMoney", "id:"+id+" project_id:"+project_id+" message:"+message+" payment:"+payment+" "+year+"年"+month+"月");
                Money money=new Money();
                money.setId(id);
                money.setProject_id(project_id);
                money.setPayment(payment);
                money.setMessage(message);
                money.setYear(year);
                money.setMonth(month);
                money.setDay(day);
                moneyList.add(money);
                if(payment<0){
                    all_pay=all_pay+payment;//支出
                }else {
                    surplus=surplus+payment;//收入
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        surplus=surplus+all_pay;//结余
        money_all_pay.setText(""+all_pay);
        money_surplus.setText(""+surplus);
        //实现列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recycler_money.setLayoutManager(layoutManager);
        adapter=new MoneyAdapter(moneyList);
        recycler_money.setAdapter(adapter);
        //添加事件监听
        money_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project_id=projectActivity.getProject_id();
                if(project_id==0){
                    Toast.makeText(getContext(),"先添加项目!",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pay=money_payment.getText().toString();
                if(pay.equals("")){
                    Toast.makeText(getContext(),"资金数目不能为空!",Toast.LENGTH_SHORT).show();
                }else {
                    add_money();
                    money_payment.setText("");
                    money_message.setText("");
                }
            }
        });
    }
    private void add_money(){
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //添加数据到数据库
        PersonalDatabaseHelper dbHelper=projectActivity.getDbHelper();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        float payment=Float.parseFloat(money_payment.getText().toString());
        String message=money_message.getText().toString();
        ContentValues values=new ContentValues();
        values.put("project_id",project_id);
        values.put("message",message);
        values.put("payment",payment);
        values.put("year",year);
        values.put("month",month);
        values.put("day",day);
        db.insert("Money",null,values);
        values.clear();
        //更新成员列表
        Money money=new Money();
        money.setProject_id(project_id);
        money.setPayment(payment);
        money.setMessage(message);
        money.setYear(year);
        money.setMonth(month);
        money.setDay(day);
        adapter.addItem(money);
        //更新统计结果
        if(payment<0){
            all_pay=all_pay+payment;
        }
        surplus=surplus+payment;
        money_all_pay.setText(""+all_pay);
        money_surplus.setText(""+surplus);
    }
}