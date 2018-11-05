package com.example;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Activity implements Serializable {
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
