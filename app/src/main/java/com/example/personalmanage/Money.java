package com.example.personalmanage;

public class Money {
    private int id;
    private int project_id;
    private String message;
    private float payment;
    private int year;
    private int month;
    private int day;

    public void setId(int id) {
        this.id = id;
    }
    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setPayment(float payment) {
        this.payment = payment;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }
    public int getProject_id() {
        return project_id;
    }
    public String getMessage() {
        return message;
    }
    public float getPayment() {
        return payment;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
}
