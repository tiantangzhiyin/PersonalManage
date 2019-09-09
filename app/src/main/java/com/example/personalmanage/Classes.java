package com.example.personalmanage;

import java.io.Serializable;

public class Classes implements Serializable {
    private String id;
    private String name;
    private String teacher;
    private String room;
    private int start_week;
    private int end_week;
    private int[] week;
    private int[] start_time;
    private int[] end_time;
    private int count;

    public Classes(String id,String name,int count){
        this.id=id;
        this.name=name;
        this.count=count;
        week=new int[count];
        start_time=new int[count];
        end_time=new int[count];
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public void setStart_week(int start_week) {
        this.start_week = start_week;
    }
    public void setEnd_week(int end_week) {
        this.end_week = end_week;
    }
    public void setWeek(int position,int week) {
        this.week[position]= week;
    }
    public void setStart_time(int position,int start_time) {
        this.start_time[position] = start_time;
    }
    public void setEnd_time(int position,int end_time) {
        this.end_time[position] = end_time;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getTeacher() {
        return teacher;
    }
    public String getRoom() {
        return room;
    }
    public int getStart_week() {
        return start_week;
    }
    public int getEnd_week() {
        return end_week;
    }
    public int getWeek(int position) {
        return week[position];
    }
    public int getStart_time(int position) {
        return start_time[position];
    }
    public int getEnd_time(int position) {
        return end_time[position];
    }
    public int getCount(){
        return count;
    }
}
