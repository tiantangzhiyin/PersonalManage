package com.example.personalmanage;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectActivity extends AppCompatActivity {
    private int project_id;
    private Boolean isChange;
    private TextView[] titles;
    private Fragment[] projectfragments;
    private int lastfragment;//用于记录上个选择的Fragment
    private PersonalDatabaseHelper dbHelper;

    public void setProject_id(int id){
        this.project_id=id;
    }
    public int getProject_id(){
        return project_id;
    }
    public void setIsChange(Boolean isChange){
        this.isChange=isChange;
    }
    public Boolean getIsChange() {
        return isChange;
    }
    public PersonalDatabaseHelper getDbHelper() {
        return dbHelper;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project);
        setSupportActionBar(toolbar);

        titles=new TextView[3];
        titles[0]=(TextView)findViewById(R.id.pro_title_main);
        titles[1]=(TextView)findViewById(R.id.pro_title_money);
        titles[2]=(TextView)findViewById(R.id.pro_title_member);

        initData();
        initClickListener();
    }
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.project_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            /*case R.id.project_submit:
                submit();
                break;*/
            case R.id.project_delete:
                delete();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){//重载返回按钮功能
        if(isChange){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    private void initData(){
        isChange=false;
        Intent intent=getIntent();
        project_id=intent.getIntExtra("project_id",0);
        dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);

        Fragment fragment1 = new ProjectMainFragment();
        Fragment fragment2 = new ProjectMoneyFragment();
        Fragment fragment3 = new ProjectMemberFragment();
        projectfragments = new Fragment[]{fragment1, fragment2, fragment3};
        lastfragment = 0;
        titles[0].setBackgroundColor(ContextCompat.getColor(this,R.color.colorTitelChick));
        getSupportFragmentManager().beginTransaction().replace(R.id.act_project_fragmentlayout, projectfragments[0]).show(projectfragments[0]).commit();
    }
    private void initClickListener(){
        titles[0].setOnClickListener(ocl);
        titles[1].setOnClickListener(ocl);
        titles[2].setOnClickListener(ocl);
    }
    private void switchFragment(int index) {
        //切换Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(projectfragments[lastfragment]);//隐藏上个Fragment
        if (!projectfragments[index].isAdded()) {
            transaction.add(R.id.act_project_fragmentlayout, projectfragments[index]);
        }
        transaction.show(projectfragments[index]).commitAllowingStateLoss();
        //切换按钮颜色
        for(int i=0;i<3;i++){
            if(i==index){
                titles[i].setBackgroundColor(ContextCompat.getColor(this,R.color.colorTitelChick));
            }else {
                titles[i].setBackgroundColor(ContextCompat.getColor(this,R.color.colorTitelNoChick));
            }
        }
        lastfragment=index;
    }
    private View.OnClickListener ocl=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.pro_title_main:
                    switchFragment(0);
                    break;
                case R.id.pro_title_money:
                    switchFragment(1);
                    break;
                case R.id.pro_title_member:
                    switchFragment(2);
                    break;
            }
        }
    };
    /*
    private void submit(){
        ProjectMainFragment fragment=(ProjectMainFragment)getSupportFragmentManager().findFragmentById(R.id.act_project_fragmentlayout);
        fragment.update_project();
        Toast.makeText(this,"项目信息已保存!",Toast.LENGTH_SHORT).show();
    }*/
    //删除项目
    private  void delete(){
        if(project_id!=0){
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            db.delete("Project","id=?",new String[]{""+project_id});
            db.delete("Money","project_id=?",new String[]{""+project_id});
            db.delete("Member","project_id=?",new String[]{""+project_id});
            Toast.makeText(this,"删除成功!",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }else {
            Toast.makeText(this,"项目不存在!",Toast.LENGTH_SHORT).show();
        }
    }
}
