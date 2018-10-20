package com.example;

import java.io.Serializable;

public class Sport implements Serializable {
    public Sport() {

    }
    public Sport(String name, int calorie) {
        this.name = name;
        this.calorie = calorie;
    }
    public String name;
    public int calorie;
}
