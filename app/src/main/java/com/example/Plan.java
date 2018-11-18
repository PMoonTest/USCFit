package com.example;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Plan implements Serializable {
    public Timestamp date;
    public String name;
    public List<Object> activity = new ArrayList<>();

}