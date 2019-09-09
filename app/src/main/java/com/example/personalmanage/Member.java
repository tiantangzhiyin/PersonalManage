package com.example.personalmanage;

public class Member {
    private int id;
    private int project_id;
    private String name;
    private String number;
    private String rank;

    public Member(int id,int project_id,String name){
        this.id=id;
        this.project_id=project_id;
        this.name=name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }
    public int getProject_id() {
        return project_id;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public String getRank() {
        return rank;
    }
}
