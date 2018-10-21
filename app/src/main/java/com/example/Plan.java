package com.example;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    public Timestamp date;
    public String name;
    public List<Object> activity = new ArrayList<>();
}
