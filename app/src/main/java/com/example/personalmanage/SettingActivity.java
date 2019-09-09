package com.example.personalmanage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    private Boolean isChange;
    private Switch key_login;
    private Switch key_remind_class;
    private TextView text_reset;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);

        key_login=(Switch)findViewById(R.id.setting_login);
        key_remind_class=(Switch)findViewById(R.id.setting_remind_class);
        text_reset=(TextView)findViewById(R.id.setting_reset);

        isChange=false;
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isLogin=pref.getBoolean("isLogin",false);
        if(isLogin){
            key_login.setChecked(true);
        }else {
            key_login.setChecked(false);
        }
        key_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor=pref.edit();
                if (isChecked){
                    editor.putBoolean("isFirst",true);
                    editor.putBoolean("isLogin",true);
                    editor.apply();
                    Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(SettingActivity.this,"开启应用锁",Toast.LENGTH_SHORT).show();
                }else {
                    editor.putBoolean("isLogin",false);
                    editor.apply();
                    Toast.makeText(SettingActivity.this,"关闭应用锁",Toast.LENGTH_SHORT).show();
                }
                isChange=true;
            }
        });

        Boolean isRemindClass=pref.getBoolean("isAddRemindClass",true);
        if(isRemindClass){
            key_remind_class.setChecked(true);
        }else{
            key_remind_class.setChecked(false);
        }
        key_remind_class.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor=pref.edit();
                if (isChecked){
                    editor.putBoolean("isAddRemindClass",true);
                    editor.apply();
                }else {
                    editor.putBoolean("isAddRemindClass",false);
                    editor.apply();
                }
                isChange=true;
            }
        });

        text_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
                isChange=true;
            }
        });
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
    private void resetData(){
        PersonalDatabaseHelper dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("Project",null,null);
        db.delete("Money",null,null);
        db.delete("Member",null,null);
        db.delete("Culture",null,null);
        db.delete("Class",null,null);
        db.delete("ClassTime",null,null);
        db.delete("Schedule",null,null);
        db.delete("Remind",null,null);
        Toast.makeText(this,"数据清空完毕",Toast.LENGTH_SHORT).show();
    }
    /*
    private boolean deleteSingleFile() {
        String filePath$Name="/data/data/com.example.personalmanage/databases/PersonalManage.db";
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Toast.makeText(this, "数据删除成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "删除文件失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }*/
}
