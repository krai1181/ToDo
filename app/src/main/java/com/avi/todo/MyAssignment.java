package com.avi.todo;

import java.io.Serializable;

public class MyAssignment implements Serializable {
    String item;
    String date;
    String time;

    public MyAssignment() {
    }

    public MyAssignment(String item, String date, String time) {
        this.item = item;
        this.date = date;
        this.time = time;
    }

    public MyAssignment(String item, String date) {
        this.item = item;
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public String getDate() {
        return date;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
