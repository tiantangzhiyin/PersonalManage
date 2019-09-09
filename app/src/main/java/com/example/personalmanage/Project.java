package com.example.personalmanage;

public class Project {
    private int project_id;
    private String project_name;
    private String master_name;
    private String project_type;

    public Project(String project_name,String master_name){
        this.project_name=project_name;
        this.master_name=master_name;
    }

    public void setProject_id(int id){
        this.project_id=id;
    }
    public void setProject_type(String type){
        this.project_type=type;
    }

    public int getProject_id(){
        return project_id;
    }
    public String getProject_name(){
        return project_name;
    }
    public String getProject_type(){
        return project_type;
    }
    public String getMaster_name(){
        return master_name;
    }
}
