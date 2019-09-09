package com.example.personalmanage;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CultureActivity extends AppCompatActivity {
    private int call_type;
    private Culture data;
    private PersonalDatabaseHelper dbHelper;
    private ImageView imageView;
    private EditText edit_title;
    private EditText edit_content;
    private String image_path;
    public static final int CHOOSE_IMAGE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_culture);
        setSupportActionBar(toolbar);

        imageView=(ImageView)findViewById(R.id.act_culture_image);
        edit_title=(EditText)findViewById(R.id.act_culture_title);
        edit_content=(EditText)findViewById(R.id.act_culture_content);

        initData();
    }
    private void initData(){
        dbHelper=new PersonalDatabaseHelper(this,"PersonalManage.db",null,1);
        Intent intent=getIntent();
        call_type=intent.getIntExtra("call_type",0);
        if(call_type==1){
            data=(Culture)intent.getSerializableExtra("data");
            String title=data.getTitle();
            String content=data.getContent();
            String image=data.getImage();
            image_path=image;
            edit_title.setText(title);
            edit_content.setText(content);
            //若图片路径非空则加载图片
            if(image==null||image.equals("")){
                Toast.makeText(this,"no image",Toast.LENGTH_SHORT).show();
            }else {
                //image为图片的路径
                //edit_title.setBackground(drawable);
                //edit_title.setBackgroundResource(R.drawable.ic_launcher_foreground);
                displayImage(image);
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {//加载菜单
        getMenuInflater().inflate(R.menu.culture_menu,menu);
        return true;//true显示菜单,false不显示
    }
    public boolean onOptionsItemSelected(MenuItem item){//定义菜单响应事件
        switch (item.getItemId()){
            case R.id.culture_submit:
                submit();
                break;
            case R.id.culture_delete:
                delete();
                break;
            case R.id.culture_choose_image:
                //动态获取权限，选择相片
                if(ContextCompat.checkSelfPermission(CultureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CultureActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;
        }
        return true;
    }
    //打开相册
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_IMAGE);
    }
    //提交修改
    private void submit(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        String title=edit_title.getText().toString();
        String content=edit_content.getText().toString();
        String image=image_path;
        values.put("title",title);
        values.put("content",content);
        values.put("image",image);
        if(call_type==0){
            db.insert("Culture",null,values);
        }else if(call_type==1){
            int id=data.getId();
            db.update("Culture",values,"id=?",new String[]{""+id});
        }
        setResult(RESULT_OK);
        finish();
    }
    //删除活动
    private void delete(){
        if(call_type==1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int id = data.getId();
            db.delete("Culture", "id=?", new String[]{""+id});//删除活动信息
            Toast.makeText(this,"data delete",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }
    @Override
    public void onBackPressed(){//重载返回按钮功能
        setResult(RESULT_CANCELED);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){//处理权限请求结果
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){//获取权限成功
                    openAlbum();
                }else{//拒绝给予权限
                    Toast.makeText(this,"你拒绝了请求",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//处理相片选择结果
        switch (requestCode){
            case CHOOSE_IMAGE:
                if (resultCode==RESULT_OK){
                    //判断手机系统版本号，处理图片
                    if(Build.VERSION.SDK_INT>=19){//4.4以上系统
                        handleImageOnKitKat(data);
                    }else {//4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        Log.d("ImageTest", "URI : "+uri);
        if(DocumentsContract.isDocumentUri(this,uri)){
            //若是document类型的URI，则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            Log.d("ImageTest", "URI type: document "+docId);
            //if("com.android.media.documents".equals(uri.getAuthority())){
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                //若URI的Authority是media格式，document id还需通过字符串分割才能得到真正的数字id
                Log.d("ImageTest", "URI type: document media "+docId);
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Log.d("ImageTest", "URI type: document providers "+docId);
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }else{
                Log.d("ImageTest", "URI type: others document "+uri);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //若是content类型的URI，则使用普通方法处理
            Log.d("ImageTest", "URI type: content "+uri);
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //若是file类型的URI，直接获取图片路径
            Log.d("ImageTest", "URI type: file "+uri);
            imagePath=uri.getPath();
        }else{
            Log.d("ImageTest", "URI type: others type "+uri);
        }
        displayImage(imagePath);//根据图片路径显示图片
        /*
        //展示图片，但不能知道真实路径
        ContentResolver resolver = getContentResolver();
        Uri originalUri = data.getData();        //获得图片的uri
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            imageView.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过uri和selection获取图片真实路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //显示图片
    private void displayImage(String imagePath){
        if (imagePath!=null){
            //Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            Bitmap bitmap=getLoacalBitmap(imagePath);
            imageView.setImageBitmap(bitmap);
            image_path=imagePath;
            Log.d("ImageTest", "displayImage: "+imagePath);
            Toast.makeText(this,"成功设置图片",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap getLoacalBitmap(String imagePath){
        try {
            FileInputStream fis = new FileInputStream(imagePath);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
