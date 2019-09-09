package com.example.personalmanage;

public class Remind {
    private int id;
    private String type;
    private String title;
    private String content;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Remind(){
        id=0;
        hour=0;
        minute=0;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
}
