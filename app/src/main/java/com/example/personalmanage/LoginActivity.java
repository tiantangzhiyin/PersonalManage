package com.example.personalmanage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText edit_password;
    private Button but_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_password=(EditText)findViewById(R.id.login_password);
        but_login=(Button)findViewById(R.id.login_submit);

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isLogin=pref.getBoolean("isLogin",false);
        if(!isLogin){//未开启应用锁直接到主界面
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_password=edit_password.getText().toString();
                Boolean isFirst=pref.getBoolean("isFirst",false);
                String password=pref.getString("password","123456");
                if(isFirst){//第一次设置密码
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("password",input_password);
                    editor.putBoolean("isFirst",false);
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"已记住密码",Toast.LENGTH_SHORT).show();
                    finish();
                }else {//其他情况登录
                    if(password.equals(input_password)){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });
    }
}
