package com.example.personalmanage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CultureAdapter extends ArrayAdapter<Culture> {
    private int resourceId;

    public CultureAdapter(Context context, int textViewResourceId, List<Culture> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){//getView()方法在每个子项滚动到屏幕内时被调用
        Culture culture=getItem(position);//获取当前项的实例
        View view;
        Viewholder viewholder;

        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewholder=new Viewholder();
            viewholder.imageView=(ImageView) view.findViewById(R.id.culture_image);
            viewholder.text_title=(TextView) view.findViewById(R.id.culture_title);
            viewholder.text_content=(TextView) view.findViewById(R.id.culture_content);
            view.setTag(viewholder);//将Viewholder存储在View中
        }else{
            view=convertView;
            viewholder=(Viewholder) view.getTag();//重新获取Viewholder
        }
        //viewholder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        String imagePath=culture.getImage();
        if(imagePath!=null&&!imagePath.equals("")){
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            viewholder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewholder.imageView.setImageBitmap(bitmap);
        }
        viewholder.text_title.setText(culture.getTitle());
        viewholder.text_content.setText(culture.getContent());
        return view;
    }
    class Viewholder{
        ImageView imageView;
        TextView text_title;
        TextView text_content;
    }
}
