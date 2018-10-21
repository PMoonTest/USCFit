package com.example;

import com.google.firebase.Timestamp;

public class Activity {
    public String name;
    public Timestamp start;
    public Timestamp end;

    public  Activity(){}

    public Activity(String name, Timestamp start, Timestamp end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
