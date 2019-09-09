package com.example.personalmanage;

public class ClassTime {
    private String []ids;//大节的一周课程id
    private String class1;//第一小节
    private String class2;//第二小节
    private String text_Mon;//星期一
    private String text_Tue;//星期二
    private String text_Wed;//星期三
    private String text_Thu;//星期四
    private String text_Fri;//星期五
    private String text_Sat;//星期六
    private String text_Sun;//星期日

    public ClassTime(String class1,String class2){
        this.class1=class1;
        this.class2=class2;
        this.ids=new String[7];
    }

    public void setText_Mon(String text_Mon) {
        this.text_Mon = text_Mon;
    }
    public void setText_Tue(String text_Tue) {
        this.text_Tue = text_Tue;
    }
    public void setText_Wed(String text_Wed) {
        this.text_Wed = text_Wed;
    }
    public void setText_Thu(String text_Thu) {
        this.text_Thu = text_Thu;
    }
    public void setText_Fri(String text_Fri) {
        this.text_Fri = text_Fri;
    }
    public void setText_Sat(String text_Sat) {
        this.text_Sat = text_Sat;
    }
    public void setText_Sun(String text_Sun) {
        this.text_Sun = text_Sun;
    }
    public void setIds(int position,String id){
        this.ids[position]=id;
    }

    public String getClass1() {
        return class1;
    }
    public String getClass2() {
        return class2;
    }
    public String getText_Mon() {
        return text_Mon;
    }
    public String getText_Tue() {
        return text_Tue;
    }
    public String getText_Wed() {
        return text_Wed;
    }
    public String getText_Thu() {
        return text_Thu;
    }
    public String getText_Fri() {
        return text_Fri;
    }
    public String getText_Sat() {
        return text_Sat;
    }
    public String getText_Sun() {
        return text_Sun;
    }
    public String getIds(int position){
        return ids[position];
    }
}
