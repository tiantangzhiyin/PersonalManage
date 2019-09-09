package com.example.personalmanage;

import java.io.Serializable;

public class Schedule implements Serializable{
    private int id;
    private String title;
    private String content;
    private String greateTime;

    public Schedule(int id,String title,String time){
        this.id=id;
        this.title=title;
        this.greateTime=time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setGreateTime(String greateTime) {
        this.greateTime = greateTime;
    }

    public int getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getGreateTime() {
        return greateTime;
    }
}
