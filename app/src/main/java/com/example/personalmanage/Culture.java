package com.example.personalmanage;

import java.io.Serializable;

public class Culture implements Serializable{
    private int id;
    private String title;
    private String content;
    private String image;

    public Culture(int id,String title){
        this.id=id;
        this.title=title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getImage() {
        return image;
    }
}
